package org.example;
import com.mysql.cj.x.protobuf.MysqlxPrepare;

import java.sql.*;
import java.util.Scanner;

class MainApp {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/labpj";
        String sql
                ="select * from persoane";
        Connection connection= DriverManager.getConnection(url, "root", "admin");
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next())
            System.out.println("id=" + rs.getInt("Id") + ", nume= "
                    + rs.getString("nume") + ",varsta=" + rs.getInt("varsta"));

        //1.
//        Scanner scanner= new Scanner(System.in);
//        System.out.println("Introduceti nume");
//        String nume = scanner.next();
//        System.out.println("Intoduceti varsta");
//        int varsta = scanner.nextInt();
//        adaugare(connection,nume,varsta);

//2. adaugare excursie
 //       adaugare_excursie(connection);
//3. afisare excursii + pers
        afisarePersoaneExcursii(connection);

        //4.
        afisareexcursiiDupaNume(connection,"Oana");
        connection.close();
        statement.close();
        rs.close();
    }

    public static void adaugare(Connection connection, String nume, int varsta)
    {
        String sql = "insert into persoane(nume,varsta) values(?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setString(1, nume);
            ps.setInt(2,varsta);

            int nr_randuri = ps.executeUpdate();
            System.out.println("Numar randuri afectate de modificare: "+ nr_randuri);
        } catch (SQLException e) {
            System.out.println(sql);
            e.printStackTrace();
        }
    }

    public static void adaugare_excursie (Connection connection)
    {
        try {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduceți id-ul persoanei: ");
        int id_pers = scanner.nextInt();

        String verificaPersoanaQuery = "SELECT * FROM persoane WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(verificaPersoanaQuery);
        ps.setInt(1, id_pers);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            scanner.nextLine();
            System.out.print("Introduceți destinația excursiei: ");
            String destinatie = scanner.nextLine();

            System.out.print("Introduceți anul excursiei: ");
            int an = scanner.nextInt();

            String adaugaExcursieQuery = "INSERT INTO excursie (destinatie, an, id_persoana) VALUES (?, ?,?)";
            ps = connection.prepareStatement(adaugaExcursieQuery);

            ps.setString(1, destinatie);
            ps.setInt(2, an);
            ps.setInt(3, id_pers);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Excursie adăugată cu succes pentru " + id_pers);
            }
        } else {
            System.out.println("Persoana nu există în tabelul de persoane. Adăugați persoana mai întâi.");
        }

        scanner.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }

    public static void afisarePersoaneExcursii(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        String sql ="select * from persoane";
        ResultSet rs = statement.executeQuery(sql);
        Statement statement2 = connection.createStatement();
        String sql2 ="SELECT * FROM excursie WHERE id_persoana = ?";

        while(rs.next())
        {
            PreparedStatement ps = connection.prepareStatement(sql2);
            ps.setInt(1, rs.getInt(1));
            ResultSet rs2 = ps.executeQuery();

            System.out.println("id=" + rs.getInt("Id") + ", nume= "
                    + rs.getString("nume") + ",varsta=" + rs.getInt("varsta" )+" \nExcursii: ");

                while(rs2.next())
                {
                    System.out.println("id_excursie=" + rs2.getInt(1) + ", destinatie= "
                            + rs2.getString(2) + ",an=" + rs2.getInt(3));
                    System.out.println("");}

        }
    }

    public static void afisareexcursiiDupaNume(Connection connection, String nume) throws SQLException {

        String sql ="select * from persoane where nume = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1,nume);
        ResultSet rs = ps.executeQuery(sql);
        String sql2= "select * from excursie where id_persoana = ?";
        while(rs.next())
        {
            PreparedStatement ps2 = connection.prepareStatement(sql2);
            ps.setInt(1, rs.getInt(1));
            ResultSet rs2 = ps.executeQuery();
            while (rs2.next())
            {
                System.out.println("id_excursie=" + rs2.getInt(1) + ", destinatie= "
                        + rs2.getString(2) + ",an=" + rs2.getInt(3));
                System.out.println("");}
            }
        }
}