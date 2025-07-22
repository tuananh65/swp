@echo off
setlocal ENABLEDELAYEDEXPANSION

REM Số lần commit mỗi lần chạy
set count=3

REM Di chuyển vào thư mục repo (nếu cần)
cd /d C:\Users\Admin\swp1\swp

REM Lặp và tạo commit
for /L %%i in (1,1,%count%) do (
    echo Tự động cập nhật %%i vào %date% %time%>> log.txt
    git add .
    git commit -m "Commit tự động %%i lúc %time%"
)

REM Push lên nhánh auto-commit
git push origin auto-commit
