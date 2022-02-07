package com.zjy.mvc.common.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.mgt.SecurityManager;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {

    // region 拦截

    /**
     * 拦截
     *
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/login");
        //未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/index");
        // 授权失败后操作
        shiroFilterFactoryBean.getFilters().put(DefaultFilter.authc.name(), new CustomFormAuthenticationFilter());
        //拦截器.
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 配置不会被拦截的链接 顺序判断，因为前端模板采用了thymeleaf，这里不能直接使用 ("/static/**", "anon")来配置匿名访问，必须配置到每个静态目录
        // https://blog.csdn.net/wanliangsoft/article/details/86533754
        // org.apache.shiro.web.filter.authc;org.apache.shiro.web.filter.authz;org.apache.shiro.web.filter.session
        filterChainDefinitionMap.put("/favicon.ico", DefaultFilter.anon.name());
        filterChainDefinitionMap.put("/user/login**", DefaultFilter.anon.name());
        filterChainDefinitionMap.put("/user/logout**", DefaultFilter.anon.name());
        filterChainDefinitionMap.put("/druid/**", DefaultFilter.anon.name());
        filterChainDefinitionMap.put("/static/**", DefaultFilter.anon.name());
        filterChainDefinitionMap.put("/comm/getEnums", DefaultFilter.anon.name());
        filterChainDefinitionMap.put("/tool/getDriverUrlList", DefaultFilter.anon.name());
        filterChainDefinitionMap.put("/tool/getTableInfo", DefaultFilter.anon.name());
        filterChainDefinitionMap.put("/echarts/**", DefaultFilter.anon.name());
        filterChainDefinitionMap.put("/user/loginpage", DefaultFilter.anon.name());
        filterChainDefinitionMap.put("/learn/angulardemo", DefaultFilter.anon.name());
        filterChainDefinitionMap.put("/learn/testangluar", DefaultFilter.anon.name());
        filterChainDefinitionMap.put("/bootstrap/**", DefaultFilter.anon.name());
        filterChainDefinitionMap.put("/js/**", DefaultFilter.anon.name());
        filterChainDefinitionMap.put("/403", DefaultFilter.anon.name());
        filterChainDefinitionMap.put("/logout", DefaultFilter.logout.name());
        filterChainDefinitionMap.put("/**", DefaultFilter.authc.name());
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager(Realm myShiroRealm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm);
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    @Bean
    public SessionManager sessionManager(SimpleCookie simpleCookie, SessionDAO sessionDAO) {
        DefaultWebSessionManager SessionManager = new DefaultWebSessionManager();
        SessionManager.setSessionIdCookie(simpleCookie);
        SessionManager.setSessionIdCookieEnabled(true);
        SessionManager.setSessionDAO(sessionDAO);
        return SessionManager;
    }

    @Bean
    public SimpleCookie simpleCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("JSESSIONID");
        simpleCookie.setPath("/");
        return simpleCookie;
    }

    @Bean
    public SessionDAO sessionDAO(CacheManager cacheManager) {
        EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
        sessionDAO.setCacheManager(cacheManager);
        return sessionDAO;
    }
    // endregion

    // region 认证

    /**
     * 认证
     *
     * @param credentialsMatcher
     * @param cacheManager
     * @return
     */
    @Bean
    public Realm myShiroRealm(CredentialsMatcher credentialsMatcher, CacheManager cacheManager) {
        MyAuthorizingRealm realm = new MyAuthorizingRealm();
        realm.setCredentialsMatcher(credentialsMatcher);
        realm.setCacheManager(cacheManager);
        realm.setAuthenticationTokenClass(UsernamePasswordToken.class);
        return realm;
    }

    /**
     * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     * ）
     *
     * @return
     */
    @Bean
    public CredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("MD5");//散列算法:这里使用MD5算法;
        credentialsMatcher.setHashIterations(1024);//散列的次数，比如散列两次，相当于 md5(md5(""));
        return credentialsMatcher;
    }

    @Bean
    public MemoryConstrainedCacheManager memoryConstrainedCacheManager() {
        return new MemoryConstrainedCacheManager();
    }
    // endregion

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

//    @Bean(name="simpleMappingExceptionResolver")
//    public SimpleMappingExceptionResolver createSimpleMappingExceptionResolver() {
//        SimpleMappingExceptionResolver r = new SimpleMappingExceptionResolver();
//        Properties mappings = new Properties();
//        mappings.setProperty("DatabaseException", "databaseError");//数据库异常处理
//        mappings.setProperty("UnauthorizedException","/user/403");
//        r.setExceptionMappings(mappings);  // None by default
//        r.setDefaultErrorView("error");    // No default
//        r.setExceptionAttribute("exception");     // Default is "exception"
//        //r.setWarnLogCategory("example.MvcLogger");     // No default
//        return r;
//    }
}
