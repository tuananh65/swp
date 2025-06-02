-- Tạo bảng Users
CREATE TABLE Users (
    UserID INT PRIMARY KEY IDENTITY(1,1),
    FullName NVARCHAR(100) NOT NULL,
    Email NVARCHAR(100) NOT NULL UNIQUE,
    Password NVARCHAR(255) NOT NULL,
    Role NVARCHAR(50) NOT NULL, -- Thay ENUM bằng NVARCHAR
    ClassName NVARCHAR(50),
    School NVARCHAR(100),
    Phone NVARCHAR(15)
);

-- Tạo bảng Courses
CREATE TABLE Courses (
    CourseID INT PRIMARY KEY IDENTITY(1,1),
    CourseName NVARCHAR(100) NOT NULL,
    TeacherID INT NOT NULL,
    StartDate DATE NOT NULL,
    EndDate DATE NOT NULL,
    FOREIGN KEY (TeacherID) REFERENCES Users(UserID)
);

-- Tạo bảng Registrations
CREATE TABLE Registrations (
    RegistrationID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT NOT NULL,
    CourseID INT NOT NULL,
    Status NVARCHAR(50) DEFAULT 'Pending', -- Thay ENUM bằng NVARCHAR
    Comments TEXT,
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (CourseID) REFERENCES Courses(CourseID)
);

-- Tạo bảng Exams
CREATE TABLE Exams (
    ExamID INT PRIMARY KEY IDENTITY(1,1),
    CourseID INT NOT NULL,
    Date DATE NOT NULL,
    Room NVARCHAR(50) NOT NULL,
    FOREIGN KEY (CourseID) REFERENCES Courses(CourseID)
);

-- Tạo bảng Results
CREATE TABLE Results (
    ResultID INT PRIMARY KEY IDENTITY(1,1),
    ExamID INT NOT NULL,
    UserID INT NOT NULL,
    Score DECIMAL(5, 2) NOT NULL,
    PassStatus BIT NOT NULL, -- Dùng BIT thay cho BOOLEAN
    FOREIGN KEY (ExamID) REFERENCES Exams(ExamID),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- Tạo bảng Certificates
CREATE TABLE Certificates (
    CertificateID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT NOT NULL,
    IssuedDate DATE NOT NULL,
    ExpirationDate DATE NOT NULL,
    CertificateCode NVARCHAR(50) UNIQUE,
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- Tạo bảng Notifications
CREATE TABLE Notifications (
    NotificationID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT NOT NULL,
    Message TEXT NOT NULL,
    SentDate DATETIME DEFAULT GETDATE(), -- Thay CURRENT_TIMESTAMP bằng GETDATE()
    IsRead BIT DEFAULT 0, -- Dùng BIT thay cho BOOLEAN
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

