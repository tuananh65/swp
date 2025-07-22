@echo off
setlocal ENABLEDELAYEDEXPANSION

REM Số lần commit mỗi lần chạy
set count=3

REM Di chuyển vào thư mục repo (nếu cần)
cd /d C:\Users\Admin\swp1\swp

REM Lặp và tạo commit
for /L %%i in (1,1,%count%) do (
    echo Update %%i at %date% %time%>> log.txt
    git add .
    git commit -m "Modify QuizDAO"
)

REM Push lên nhánh update-dal
git push origin update-dal
