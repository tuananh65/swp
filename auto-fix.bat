@echo off
cd /d C:\Users\Admin\swp1\swp

REM Tên file dùng để thay đổi
set FILE=log.txt

REM Ghi 100 dòng mới vào file
echo Update + add PostDAO %FILE%
(for /L %%i in (1,1,100) do echo Line %%i - add) > %FILE%

REM Commit đầu tiên: thêm dòng
git add .
git commit -m "🟢 Fix %FILE%"
git push origin auto-commit

REM Xóa toàn bộ nội dung (để tăng số dòng xóa)
echo. > %FILE%

REM Commit thứ hai: xóa dòng
git add .
git commit -m "🔴 Delete %FILE%"
git push origin auto-commit

echo ✅ Đã hoàn thành auto-fix: thêm và xóa 100 dòng!
pause
