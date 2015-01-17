# ---------- USERS ----------

INSERT INTO USER(idUser,Email,Groupname,Name,Password,PublicCalendar,Surname,Username,Verificationkey,Verified)
VALUES (101,'alessiorossotti@mailinator.com','USERS','Alessio','5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8',1,'Rossotti','ale888',null,1);

INSERT INTO USER(idUser,Email,Groupname,Name,Password,PublicCalendar,Surname,Username,Verificationkey,Verified)
VALUES (102,'paulpogba@mailinator.com','USERS','Paul','5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8',1,'Pogba','polpopaul',null,1);

INSERT INTO USER(idUser,Email,Groupname,Name,Password,PublicCalendar,Surname,Username,Verificationkey,Verified)
VALUES (103,'vidal23@mailinator.com','USERS','Arturo','5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8',1,'Vidal','vidal23',null,1);

INSERT INTO USER(idUser,Email,Groupname,Name,Password,PublicCalendar,Surname,Username,Verificationkey,Verified)
VALUES (104,'matteopasina@mailinator.com','USERS','Matteo','5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8',1,'Pasina','pasateo',null,1);

INSERT INTO USER(idUser,Email,Groupname,Name,Password,PublicCalendar,Surname,Username,Verificationkey,Verified)
VALUES (105,'simonerubiu@mailinator.com','USERS','Simone','5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8',1,'Rubiu','roobew',null,1);

INSERT INTO USER(idUser,Email,Groupname,Name,Password,PublicCalendar,Surname,Username,Verificationkey,Verified)
VALUES (106,'giovannimuchacho@mailinator.com','USERS','Giovanni','5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8',1,'Muchacho','giomuchacho',null,1);

INSERT INTO USER(idUser,Email,Groupname,Name,Password,PublicCalendar,Surname,Username,Verificationkey,Verified)
VALUES (107,'aidayespica@mailinator.com','USERS','Aida','5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8',1,'Yespica','yespica69',null,1);

INSERT INTO USER(idUser,Email,Groupname,Name,Password,PublicCalendar,Surname,Username,Verificationkey,Verified)
VALUES (108,'fedebuffa@mailinator.com','USERS','Federico','5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8',1,'Buffa','fedebuffon',null,1);

INSERT INTO USER(idUser,Email,Groupname,Name,Password,PublicCalendar,Surname,Username,Verificationkey,Verified)
VALUES (109,'fedenargi@mailinator.com','USERS','Federica','5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8',1,'Nargi','nargi90',null,1);

INSERT INTO USER(idUser,Email,Groupname,Name,Password,PublicCalendar,Surname,Username,Verificationkey,Verified)
VALUES (110,'stevej@mailinator.com','USERS','Steve','5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8',1,'Jobs','stevejobs',null,1);

INSERT INTO USER(idUser,Email,Groupname,Name,Password,PublicCalendar,Surname,Username,Verificationkey,Verified)
VALUES (111,'kasparov@mailinator.com','USERS','Garri','5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8',1,'Kasparov','kasparov',null,1);

# ---------- LOCATIONS ----------

INSERT INTO LOCATION(idLocation,Address,City,Latitude,Longitude,State)
VALUES (100,'Viale Romagna','Milan',45.474297, 9.223977,'Italy');

# ---------- EVENTS ----------

INSERT INTO EVENT(idEvent,Description,EndTime,Image,Name,Outdoor,Public,StartTime,idLocation,idOrganizer)
VALUES (100,'A chess tournament is a series of chess games played competitively to determine a winning individual or team.',
        '2015-01-18 18:00:00','chess.png','Chess Tournament',0,1,'2015-01-16 12:00:00',100,111);

INSERT INTO CALENDAR(idEvent,idUser) VALUES (100,111);
