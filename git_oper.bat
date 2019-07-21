@echo off

title soa2

:head
echo.
set /p type=git type: pull(1), status(2), add(3), commit(4), push(5):
if "%type%"=="1" (
  goto gitpull
)
if "%type%"=="2" (
  goto gitstatus
)
if "%type%"=="3" (
  goto gitadd
)
if "%type%"=="4" (
  goto gitcommit
)
if "%type%"=="5" (
  goto gitpush
)
echo quit
goto end

:gitpull
echo.
echo git pull
git pull
set type=
goto head

:gitadd
echo.
echo git add
git add .
set type=
goto head

:gitcommit
echo.
echo git commit
set /p commsg=commit: 
git commit -m "%commsg%"
set type=
goto head

:gitpush
echo.
echo git push
git push
set type=
goto head

:gitstatus
echo.
echo git status
git status
set type=
goto head

:end