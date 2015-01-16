# ---------- USERS ----------

INSERT INTO USER(idUser,Email,Groupname,Name,Password,PublicCalendar,Surname,Username,Verificationkey,Verified)
VALUES (101,'alessiorossotti@mailinator.com','USERS','Alessio','password',1,'Rossotti','ale888',null,1);

INSERT INTO USER(idUser,Email,Groupname,Name,Password,PublicCalendar,Surname,Username,Verificationkey,Verified)
VALUES (102,'paulpogba@mailinator.com','USERS','Paul','password',1,'Pogba','polpopaul',null,1);

INSERT INTO USER(idUser,Email,Groupname,Name,Password,PublicCalendar,Surname,Username,Verificationkey,Verified)
VALUES (103,'vidal23@mailinator.com','USERS','Arturo','password',1,'Vidal','vidal23',null,1);

INSERT INTO USER(idUser,Email,Groupname,Name,Password,PublicCalendar,Surname,Username,Verificationkey,Verified)
VALUES (104,'matteopasina@mailinator.com','USERS','Matteo','password',1,'Pasina','pasateo',null,1);

INSERT INTO USER(idUser,Email,Groupname,Name,Password,PublicCalendar,Surname,Username,Verificationkey,Verified)
VALUES (105,'simonerubiu@mailinator.com','USERS','Simone','password',1,'Rubiu','roobew',null,1);

INSERT INTO USER(idUser,Email,Groupname,Name,Password,PublicCalendar,Surname,Username,Verificationkey,Verified)
VALUES (106,'giovannimuchacho@mailinator.com','USERS','Giovanni','password',1,'Muchacho','giomuchacho',null,1);

INSERT INTO USER(idUser,Email,Groupname,Name,Password,PublicCalendar,Surname,Username,Verificationkey,Verified)
VALUES (107,'aidayespica@mailinator.com','USERS','Aida','password',1,'Yespica','yespica69',null,1);

INSERT INTO USER(idUser,Email,Groupname,Name,Password,PublicCalendar,Surname,Username,Verificationkey,Verified)
VALUES (108,'fedebuffa@mailinator.com','USERS','Federico','password',1,'Buffa','fedebuffon',null,1);

INSERT INTO USER(idUser,Email,Groupname,Name,Password,PublicCalendar,Surname,Username,Verificationkey,Verified)
VALUES (109,'fedenargi@mailinator.com','USERS','Federica','password',1,'Nargi','nargi90',null,1);

INSERT INTO USER(idUser,Email,Groupname,Name,Password,PublicCalendar,Surname,Username,Verificationkey,Verified)
VALUES (110,'stevej@mailinator.com','USERS','Steve','password',1,'Jobs','stevejobs',null,1);

INSERT INTO USER(idUser,Email,Groupname,Name,Password,PublicCalendar,Surname,Username,Verificationkey,Verified)
VALUES (111,'kasparov@mailinator.com','USERS','Garri','password',1,'Kasparov','kasparov',null,1);

# ---------- EVENTS ----------

INSERT INTO EVENT(idEvent,Description,EndTime,Image,Name,Outdoor,Public,StartTime,idLocation,idOrganizer)
VALUES (1,'A chess tournament is a series of chess games played competitively to determine a winning individual or team.
           Since the first international chess tournament in London, 1851, chess tournaments have become the standard
           form of chess competition among serious players.','USERS','chess.png','Chess Tournament',0,1,,null,111);

# ---------- LOCATIONS ----------
