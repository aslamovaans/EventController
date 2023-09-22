package com.example.eventController;

import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatabaseAdapter {
    public static final String DB_URL = "jdbc:h2:/db/Events";
    public static final String DB_Driver = "org.h2.Driver";
    public static final String TableEvent="Event";
    public static final String TableUser="People";
    public static final String TableGuest="Guest";
    public static final String TableEquipment="Equipment";
    public static final String TableGuestPeople="GuestPeople";
    public static final String TableEquipmentPeople = "EquipmentPeople";

    /**
     * get connection
     */
    public static void getDBConnection() {
        try {
            Class.forName(DB_Driver);
            Connection connection = DriverManager.getConnection(DB_URL);
            DatabaseMetaData md = connection.getMetaData();
            ResultSet rs = md.getTables(null, null, TableEvent, null);
            /*deleteTable(TableEvent);
            deleteTable(TableUser);
            deleteTable(TableGuest);
            deleteTable(TableEquipment);
            deleteTable(TableGuestPeople);
            deleteTable(TableEquipmentPeople);*/
            if (!rs.next()) {
                createTableEvent();
                createTableUser();
                createTableGuest();
                createTableEquipment();
                createTableGuestPeople();
                createTableEquipmentPeople();
            }
            connection.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("JDBC драйвер для СУБД не найден!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка SQL!");
        }
    }


    private static void deleteTable(String Table)
    {
        String deleteTableSQL = "DROP TABLE "+Table;
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.execute(deleteTableSQL);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    private static void createTableEvent() {
        String createTableSQL = "CREATE TABLE "+ TableEvent+ " ("
                + "ID INT NOT NULL auto_increment, "
                + "Date DATETIME NOT NULL, "
                + "Title varchar(100) NOT NULL, "
                + "Location varchar(50) NOT NULL, "
                + "PRIMARY KEY (ID) "
                + ")";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.execute(createTableSQL);
            }
        } catch (SQLException ignored) {
        }
    }
    private static void createTableGuest() {
        String createTableSQL = "CREATE TABLE "+ TableGuest+ " ("
                + "ID INT NOT NULL auto_increment, "
                + "Description varchar(255) NOT NULL, "
                + "count_need int NOT NULL, "
                + "count_exist int NULL, "
                + "id_event int, "
                + "PRIMARY KEY (ID), "
                + "FOREIGN KEY(id_event) references Event(ID)"
                + ")";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.execute(createTableSQL);
            }
        } catch (SQLException ignored) {
        }
    }

    private static void createTableUser() {
        String createTableSQL = "CREATE TABLE "+ TableUser+ " ("
                + "ID INT NOT NULL auto_increment, "
                + "Name varchar(20) NOT NULL UNIQUE, "
                + "Password varchar(20) NOT NULL, "
                + "Phone varchar(20) NULL, "
                + "PRIMARY KEY (ID) "
                + ")";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.execute(createTableSQL);
            }
        } catch (SQLException ignored) {
        }
    }
    private static void createTableEquipment() {
        String createTableSQL = "CREATE TABLE "+ TableEquipment + " ("
                + "ID INT NOT NULL auto_increment, "
                + "Title varchar(20) NOT NULL, "
                + "count_need int NOT NULL, "
                + "count_have int NULL, "
                + "id_event int,"
                + "PRIMARY KEY (ID) ,"
                + "FOREIGN KEY(id_event) references Event(ID)"
                + ")";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.execute(createTableSQL);
            }
        } catch (SQLException ignored) {
        }
    }
    private static void createTableGuestPeople() {
        String createTableSQL = "CREATE TABLE "+ TableGuestPeople + " ("
                + "ID INT NOT NULL auto_increment, "
                + "id_guest int NOT NULL, "
                + "id_people int NOT NULL, "
                + "PRIMARY KEY (ID) ,"
                + "FOREIGN KEY(id_guest) references Guest(ID),"
                + "FOREIGN KEY(id_people) references People(ID)"
                + ")";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.execute(createTableSQL);
            }
        } catch (SQLException ignored) {
        }
    }
    private static void createTableEquipmentPeople() {
        String createTableSQL = "CREATE TABLE "+ TableEquipmentPeople + " ("
                + "ID INT NOT NULL auto_increment, "
                + "id_equipment int NOT NULL, "
                + "id_people int NOT NULL, "
                + "count int not null, "
                + "PRIMARY KEY (ID), "
                + "FOREIGN KEY(id_equipment) references Equipment(ID),"
                + "FOREIGN KEY(id_people) references People(ID)"
                + ")";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.execute(createTableSQL);
            }
        } catch (SQLException ignored) {
        }
    }

    public static void getUsers(ObservableList<People> elementsDAO) {
        String selection = "select * from "+ TableUser;
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                ResultSet rs = statement.executeQuery(selection);
                while (rs.next()) {
                    People user = new People(rs.getString("Name"),
                            rs.getString("Password"));
                    user.id = rs.getInt("ID");
                    user.setPhone(rs.getString("Phone"));
                    elementsDAO.add(user);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int getUser(String Name,String Password) {
        String selection = "select * from "+ TableUser+" where Name='"+Name+"' AND Password='"+Password+"'";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                ResultSet rs = statement.executeQuery(selection);
                if (rs.next()) {
                    return rs.getInt("ID");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }


    public static People getUser(int id) {
        String selection = "select * from "+ TableUser+" where ID="+id;
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                ResultSet rs = statement.executeQuery(selection);
                if (rs.next()) {
                    return new People(rs.getString("Name"),
                            rs.getString("Password")).setID(rs.getInt("ID")).setPhone(rs.getString("Phone"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public static Event getEvent(int id) {
        String selection = "select * from "+ TableEvent+" where ID="+id;
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                ResultSet rs = statement.executeQuery(selection);
                if (rs.next()) {
                    return new Event.RequestBuilder(LocalDateTime.of(rs.getDate("Date").toLocalDate(),rs.getTime("Date").toLocalTime()),
                            rs.getString("Title"),rs.getString("Location")).setID(rs.getInt("ID")).build();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    public static void getEvents(ObservableList<Event> elementsDAO) {
        String selection = "select * from "+ TableEvent;
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                ResultSet rs = statement.executeQuery(selection);
                while (rs.next()) {
                    Event element = new Event.RequestBuilder(LocalDateTime.of(rs.getDate("Date").toLocalDate(),rs.getTime("Date").toLocalTime()),
                            rs.getString("Title"),rs.getString("Location")).setID(rs.getInt("ID")).build();
                    elementsDAO.add(element);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static int addUser(People user) {
        String insertTableSQL = "INSERT INTO "+ TableUser
                + " (Name,Password,Phone) " + "VALUES "
                + "('"+user.getName()+"','"+user.getPassword()+"','"+user.getPhone()+"')";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.executeUpdate(insertTableSQL);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try(Statement statement = dbConnection.createStatement()) {
                ResultSet rs=statement.executeQuery("SELECT TOP 1 ID FROM "+TableUser+" ORDER BY ID DESC");
                rs.next();
                return rs.getInt("ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }


    public static int addEvent(Event element) {
        String insertTableSQL = "INSERT INTO "+ TableEvent
                    + " (Date,Title,Location) " + "VALUES "
                    + "('"+element.dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"))+"','"+element.title+"','"+element.place+"')";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.executeUpdate(insertTableSQL);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try(Statement statement = dbConnection.createStatement()) {
                ResultSet rs=statement.executeQuery("SELECT TOP 1 ID FROM "+TableEvent+" ORDER BY ID DESC");
                rs.next();
                return rs.getInt("ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public static boolean deleteEvent(int ID) {
        String deleteTableSQL = String.format("DELETE from "+ TableEvent +" WHERE ID=%s;",ID);
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.executeUpdate(deleteTableSQL);
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    public static boolean deletePeople(int ID) {
        String deleteTableSQL = String.format("DELETE from "+ TableUser +" WHERE ID=%s;",ID);
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.executeUpdate(deleteTableSQL);
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    public static int addEquipment(Equipment element) {
        String insertTableSQL = "INSERT INTO "+ TableEquipment
                + " (Title,count_need,count_have,id_event) " + "VALUES "
                + "('"+element.title+"','"+element.count_need+"','"+element.count_exist+"',"+element.id_event+")";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.executeUpdate(insertTableSQL);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try(Statement statement = dbConnection.createStatement()) {
                ResultSet rs=statement.executeQuery("SELECT TOP 1 ID FROM "+TableEquipment+" ORDER BY ID DESC");
                rs.next();
                return rs.getInt("ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }
    public static int addGuest(Guest element) {
        String insertTableSQL = "INSERT INTO "+ TableGuest
                + " (Description,count_need,count_exist,id_event) " + "VALUES "
                + "('"+element.description+"','"+element.count_need+"','"+element.count_exist+"',"+element.id_event+")";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.executeUpdate(insertTableSQL);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try(Statement statement = dbConnection.createStatement()) {
                ResultSet rs=statement.executeQuery("SELECT TOP 1 ID FROM "+TableGuest+" ORDER BY ID DESC");
                rs.next();
                return rs.getInt("ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }
    public static int fillGuest(People people, Guest guest)
    {
        String insertTableSQL = "INSERT INTO "+ TableGuestPeople
                + " (id_guest,id_people) " + "VALUES "
                + "("+guest.id+","+people.id+")";
        String updateTableSQL = String.format("UPDATE "+ TableGuest
                        + " SET count_exist='%s' where ID=%s;",
                1+guest.count_exist,guest.id);
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.executeUpdate(insertTableSQL);
                statement.executeUpdate(updateTableSQL);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try(Statement statement = dbConnection.createStatement()) {
                ResultSet rs=statement.executeQuery("SELECT TOP 1 ID FROM "+TableGuestPeople+" ORDER BY ID DESC");
                rs.next();
                return rs.getInt("ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }
    public static int fillEquipment(People people, Equipment equipment,int count)
    {
        String insertTableSQL = "INSERT INTO "+ TableEquipmentPeople
                + " (id_equipment,id_people,count) " + "VALUES "
                + "("+equipment.id+","+people.id+","+count+")";
        String updateTableSQL = String.format("UPDATE "+ TableEquipment
                        + " SET count_have='%s' where ID=%s;",
                count+equipment.count_exist,equipment.id);
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.executeUpdate(insertTableSQL);
                statement.executeUpdate(updateTableSQL);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try(Statement statement = dbConnection.createStatement()) {
                ResultSet rs=statement.executeQuery("SELECT TOP 1 ID FROM "+TableGuestPeople+" ORDER BY ID DESC");
                rs.next();
                return rs.getInt("ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }
    public static void getEquipmentForEvent(ObservableList<Equipment> elementsDAO, Event event) {
        String selection = "select * from "+ TableEquipment+ " WHERE id_event = "+event.id;
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                ResultSet rs = statement.executeQuery(selection);
                while (rs.next()) {
                    Equipment equipment = new Equipment(rs.getString("Title"),rs.getInt("count_need"), rs.getInt("count_have"));
                    equipment.setId(rs.getInt("ID"));
                    equipment.setId_event(event.id);
                    elementsDAO.add(equipment);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static Equipment getEquipment(int id) {
        String selection = "select * from "+ TableEquipment+" where ID="+id;
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                ResultSet rs = statement.executeQuery(selection);
                if (rs.next()) {
                    Equipment equipment = new Equipment(rs.getString("Title"),rs.getInt("count_need"), rs.getInt("count_have"));
                    equipment.setId(rs.getInt("ID"));
                    equipment.setId_event(rs.getInt("id_event"));
                    return equipment;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public static Guest getGuest(int id) {
        String selection = "select * from "+ TableGuest+" where ID="+id;
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                ResultSet rs = statement.executeQuery(selection);
                if (rs.next()) {
                    Guest guest = new Guest(rs.getInt("ID"),rs.getString("Description"),rs.getInt("count_need"), rs.getInt("count_exist"));
                    guest.setId_event(rs.getInt("id_event"));
                    return guest;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public static void getGuestsForEvent(ObservableList<Guest> elementsDAO,Event event) {
        String selection = "select * from "+ TableGuest + " WHERE id_event = "+event.id;
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                ResultSet rs = statement.executeQuery(selection);
                while (rs.next()) {
                   Guest guest = new Guest(rs.getInt("ID"),rs.getString("Description"),rs.getInt("count_need"), rs.getInt("count_exist"));
                   guest.setId_event(event.id);
                   elementsDAO.add(guest);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void getPeopleGuestsForEvent(ObservableList<People> elementsDAO,Event event) {
        String selection = "select gp.id_people from "+ TableGuest + " g join "+TableGuestPeople+" gp on g.ID=gp.id_guest WHERE id_event = "+event.id;
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                ResultSet rs = statement.executeQuery(selection);
                while (rs.next()) {
                    elementsDAO.add(getUser(rs.getInt("gp.id_people")));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void getGuestsForPeople(ObservableList<GuestPeople> elementsDAO,People people) {
        String selection = "select * from "+ TableGuestPeople + " WHERE id_people = "+people.id;
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                ResultSet rs = statement.executeQuery(selection);
                while (rs.next()) {
                    elementsDAO.add(new GuestPeople(getGuest(rs.getInt("id_guest"))));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void getPeopleEquipmentForEvent(ObservableList<People> elementsDAO,Event event) {
        String selection = "select gp.id_people from "+ TableEquipment + " g join "+TableEquipmentPeople+" gp on g.ID=gp.id_equipment WHERE id_event = "+event.id;
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                ResultSet rs = statement.executeQuery(selection);
                while (rs.next()) {
                    elementsDAO.add(getUser(rs.getInt("gp.id_people")));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void getEquipmentForPeople(ObservableList<EquipmentPeople> elementsDAO,People people) {
        String selection = "select * from "+ TableEquipmentPeople + " WHERE id_people = "+people.id;
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                ResultSet rs = statement.executeQuery(selection);
                while (rs.next()) {
                    elementsDAO.add(new EquipmentPeople(getEquipment(rs.getInt("id_equipment")),rs.getInt("count")));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
