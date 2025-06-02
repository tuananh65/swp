CREATE TABLE CourseSchedules (
    ScheduleID INT PRIMARY KEY IDENTITY(1,1),
    CourseID INT FOREIGN KEY REFERENCES Courses(CourseID),
    ScheduleDateTime DATETIME NOT NULL, -- Thay ScheduleDate bằng ScheduleDateTime để lưu cả ngày và giờ
    Location NVARCHAR(255) NOT NULL -- Địa điểm học
);