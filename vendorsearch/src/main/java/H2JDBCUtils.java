import java.sql.*;

public class H2JDBCUtils {

    private static String jdbcURL = "jdbc:h2:file:./database/h2db/db/vendorsearchtool;DB_CLOSE_DELAY=-1";
    private static String jdbcUsername = "vendorsearchtool";
    private static String jdbcPassword = "";
    private static String createProductsTable = "create table IF NOT EXISTS products(id int primary key, partNumber varchar(250), title varchar(750)," +
            " description varchar(1250), packSize varchar(250), qty varchar(250), specifications varchar(250), stock varchar(250)" +
            ", stockDetails varchar(750), listPrice varchar(250), dealerPrice varchar(250), vendor varchar(250), link varchar(1250)" +
            ",result_id int)";
    private static String createResultsTable = "create table IF NOT EXISTS search_result(id int primary key, searchStr varchar(250) unique, searchTime varchar(250))";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return connection;
    }

    public static void createProductTable() {
        try (Connection connection = H2JDBCUtils.getConnection();
             // Step 2:Create a statement using connection object
             Statement statement = connection.createStatement();) {

            // Step 3: Execute the query or update query
            statement.execute(createProductsTable);

        } catch (SQLException e) {
            // print SQL exception information
            H2JDBCUtils.printSQLException(e);
        }
    }

    public static boolean isProductDetailsExists(int searchId, String partnum, String vendor) {
        String selectProductQuery = "Select * from products where result_id = '" + searchId +
                "' AND partnumber='" + partnum + "' AND vendor='"+ vendor +"';";
        try (Connection connection = H2JDBCUtils.getConnection();
             // Step 2:Create a statement using connection object
             Statement statement = connection.createStatement();) {

            // Step 3: Execute the query or update query
            ResultSet resultSet = statement.executeQuery(selectProductQuery);
            return resultSet.next();

        } catch (SQLException e) {
            // print SQL exception information
            H2JDBCUtils.printSQLException(e);
        }
        return false;
    }

    public static SearchDetails getSearchDetails(String searchstr) {
        String selectResultQuery = "Select * from search_result where searchstr='" + searchstr + "';";
        String selectProductQuery = "Select * from products where result_id = " +
                "(Select id from search_result where searchstr='" + searchstr + "');";
        SearchDetails searchDetails = null;
        try (Connection connection = H2JDBCUtils.getConnection();
             // Step 2:Create a statement using connection object
             Statement statement = connection.createStatement();) {

            // Step 3: Execute the query or update query
            ResultSet rs = statement.executeQuery(selectResultQuery);
            if (rs.next()) {
                searchDetails = new SearchDetails();
                searchDetails.setSearchStr(rs.getString("searchstr"));
                searchDetails.setSearchTime(rs.getString("searchtime"));
            }

        } catch (SQLException e) {
            // print SQL exception information
            H2JDBCUtils.printSQLException(e);
            return null;
        }
        try (Connection connection = H2JDBCUtils.getConnection();
             // Step 2:Create a statement using connection object
             Statement statement = connection.createStatement();) {

            // Step 3: Execute the query or update query
            ResultSet rs = statement.executeQuery(selectProductQuery);
            while (rs.next() && searchDetails != null) {
                ProductDetails productDetails = new ProductDetails();
                productDetails.setPartNumber(rs.getString("partnumber"));
                productDetails.setTitle(rs.getString("title"));
                productDetails.setDescription(rs.getString("description"));
                productDetails.setPackSize(rs.getString("packsize"));
                productDetails.setQty(rs.getString("qty"));
                productDetails.setSpecifications(rs.getString("specifications"));
                productDetails.setStock(rs.getString("stock"));
                productDetails.setStockDetails(rs.getString("stockdetails"));
                productDetails.setListPrice(rs.getString("listprice"));
                productDetails.setDealerPrice(rs.getString("dealerprice"));
                productDetails.setVendor(rs.getString("vendor"));
                productDetails.setLink(rs.getString("link"));
                searchDetails.getProductDetails().add(productDetails);
            }

        } catch (SQLException e) {
            // print SQL exception information
            H2JDBCUtils.printSQLException(e);
        }
        return searchDetails;
    }

    public static void insertOrUpdateSearchResult(SearchDetails searchDetails) {
        String selectResultQuery = "Select * from search_result where searchstr='" + searchDetails.getSearchStr() + "';";
        try (Connection connection = H2JDBCUtils.getConnection();
             // Step 2:Create a statement using connection object
             Statement statement = connection.createStatement();) {

            // Step 3: Execute the query or update query
            ResultSet rs = statement.executeQuery(selectResultQuery);
            if (rs.next()) {
                int id = rs.getInt("id");
                updateSearchResult(searchDetails, id);
            } else {
                insertSearchResult(searchDetails);
            }

        } catch (SQLException e) {
            // print SQL exception information
            H2JDBCUtils.printSQLException(e);
        }
    }

    private static void updateSearchResult(SearchDetails searchDetails, int searchId) {
        String updateSearchQuery = "Update search_result set searchstr='" +
                searchDetails.getSearchStr() + "', searchtime='" + searchDetails.getSearchTime() + "' where id='"
                + searchId + "'";
        try (Connection connection = H2JDBCUtils.getConnection();
             // Step 2:Create a statement using connection object
             Statement statement = connection.createStatement();) {

            // Step 3: Execute the query or update query
            statement.execute(updateSearchQuery);

        } catch (SQLException e) {
            // print SQL exception information
            H2JDBCUtils.printSQLException(e);
            return;
        }
        searchDetails.getProductDetails().forEach(productDetails -> {
            if(isProductDetailsExists(searchId, productDetails.getPartNumber(), productDetails.getVendor())) {
                updateProductDetails(productDetails, searchDetails.getSearchStr());
            }
            else {
                insertProductDetails(productDetails, searchId);
            }
        });
    }

    public static void updateProductDetails(ProductDetails productDetails, String searchStr) {
        String updateProductQuery = "update products set partnumber=?,title=?,description=?,packsize=?,qty=?,specifications=?" +
                ",stock=?,stockdetails=?,listprice=?,dealerprice=?,vendor=?,link=?" +
                " where result_id=(Select id from search_result where searchstr = '" + searchStr + "') AND partnumber=? AND vendor=?;";
        try (Connection connection = H2JDBCUtils.getConnection();
             // Step 2:Create a statement using connection object
             PreparedStatement statement = connection.prepareStatement(updateProductQuery);) {
            try {
                statement.setString(1, productDetails.getPartNumber());
                statement.setString(2, productDetails.getTitle());
                statement.setString(3, productDetails.getDescription());
                statement.setString(4, productDetails.getPackSize());
                statement.setString(5, productDetails.getQty());
                statement.setString(6, productDetails.getSpecifications());
                statement.setString(7, productDetails.getStock());
                statement.setString(8, productDetails.getStockDetails());
                statement.setString(9, productDetails.getListPrice());
                statement.setString(10, productDetails.getDealerPrice());
                statement.setString(11, productDetails.getVendor());
                statement.setString(12, productDetails.getLink());
                statement.setString(13, productDetails.getPartNumber());
                statement.setString(14, productDetails.getVendor());
                statement.execute();

            } catch (SQLException e) {
                H2JDBCUtils.printSQLException(e);
            }

        } catch (SQLException e) {
            // print SQL exception information
            H2JDBCUtils.printSQLException(e);
        }
    }

    private static void insertProductDetails(ProductDetails productDetails, int searchId) {
        String insertProductQuery = "Insert into products values ((Select coalesce(max(id),-1) + 1 from products), " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try (Connection connection = H2JDBCUtils.getConnection();
             // Step 2:Create a statement using connection object
             PreparedStatement statement = connection.prepareStatement(insertProductQuery);) {
            try {
                statement.setString(1, productDetails.getPartNumber());
                statement.setString(2, productDetails.getTitle());
                statement.setString(3, productDetails.getDescription());
                statement.setString(4, productDetails.getPackSize());
                statement.setString(5, productDetails.getQty());
                statement.setString(6, productDetails.getSpecifications());
                statement.setString(7, productDetails.getStock());
                statement.setString(8, productDetails.getStockDetails());
                statement.setString(9, productDetails.getListPrice());
                statement.setString(10, productDetails.getDealerPrice());
                statement.setString(11, productDetails.getVendor());
                statement.setString(12, productDetails.getLink());
                statement.setInt(13, searchId);
                statement.execute();

            } catch (SQLException e) {
                H2JDBCUtils.printSQLException(e);
            }

        } catch (SQLException e) {
            // print SQL exception information
            H2JDBCUtils.printSQLException(e);
        }
    }

    private static void insertSearchResult(SearchDetails searchDetails) {
        String insertSearchResultQuery = "Insert into search_result values((Select coalesce(max(id),-1) + 1 from search_result), '" +
                searchDetails.getSearchStr() + "', '" + searchDetails.getSearchTime() + "');";
        String insertProductQuery = "Insert into products values ((Select coalesce(max(id),-1) + 1 from products), " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, (Select id from search_result where searchstr = '" + searchDetails.getSearchStr() + "'));";
        try (Connection connection = H2JDBCUtils.getConnection();
             // Step 2:Create a statement using connection object
             Statement statement = connection.createStatement();) {

            // Step 3: Execute the query or update query
            statement.execute(insertSearchResultQuery);

        } catch (SQLException e) {
            // print SQL exception information
            H2JDBCUtils.printSQLException(e);
            return;
        }
        try (Connection connection = H2JDBCUtils.getConnection();
             // Step 2:Create a statement using connection object
             PreparedStatement statement = connection.prepareStatement(insertProductQuery);) {

            searchDetails.getProductDetails().forEach(productDetails -> {
                try {
                    statement.setString(1, productDetails.getPartNumber());
                    statement.setString(2, productDetails.getTitle());
                    statement.setString(3, productDetails.getDescription());
                    statement.setString(4, productDetails.getPackSize());
                    statement.setString(5, productDetails.getQty());
                    statement.setString(6, productDetails.getSpecifications());
                    statement.setString(7, productDetails.getStock());
                    statement.setString(8, productDetails.getStockDetails());
                    statement.setString(9, productDetails.getListPrice());
                    statement.setString(10, productDetails.getDealerPrice());
                    statement.setString(11, productDetails.getVendor());
                    statement.setString(12, productDetails.getLink());
                    statement.execute();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            });

        } catch (SQLException e) {
            // print SQL exception information
            H2JDBCUtils.printSQLException(e);
        }
    }


    public static void createResultTable() {
        try (Connection connection = H2JDBCUtils.getConnection();
             // Step 2:Create a statement using connection object
             Statement statement = connection.createStatement();) {

            // Step 3: Execute the query or update query
            statement.execute(createResultsTable);

        } catch (SQLException e) {
            // print SQL exception information
            H2JDBCUtils.printSQLException(e);
        }
    }


    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}