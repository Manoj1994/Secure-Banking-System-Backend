package com.bankingapp.service.requestservice;

import java.util.ArrayList;
import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.sql.DataSource;
import java.sql.ResultSet;

import com.bankingapp.model.request.Request;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class RequestService {

    private final String TABLE_NAME = "request"; // change this according to your request table name
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate; // so far I haven't use this yet - Waris

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /*
    Assumes that we have all parameters already loaded in request object
     */
    public Boolean add_new_request(Request request){
        boolean status = true;
        // set up SQL connection
        Connection conn = null;
        PreparedStatement prep = null;
        try {
            conn = dataSource.getConnection();
            // Update tuple
            String q = "insert into request(id,requester_id,request_type, current_value, requested_value, status, approver_id, description, timestamp_created, timestamp_updated) values(?,?,?,?,?,?,?,?,?,?)";
            prep = conn.prepareStatement(q);
            prep.setInt(1, request.getId());
            prep.setInt(2, request.getRequesterId());
            prep.setString(3, request.getRequest_type());
            prep.setString(4, request.getCurrent_value());
            prep.setString(5, request.getRequested_value());
            prep.setString(6, request.getStatus());
            prep.setInt(7, request.getApproverId());
            prep.setString(8, request.getDescription());

            Date date = new Date();
            Timestamp ts = new Timestamp(date.getTime());

            prep.setTimestamp(9, ts);
            prep.setString(10, null);

            int row_changed = prep.executeUpdate();
            if(row_changed != 1) {
                status = false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            status = false;
        } catch (Exception e) {
            e.printStackTrace();
            status = false;

        } finally {
            try {
                prep.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } // end of finally

        return status;
    }

    /*
    Assumes The request id is unique
    * */
    public Request getByID(int id){

        Request request = new Request();
        // set up SQL connection
        Connection conn = null;
        PreparedStatement prep = null;
        try {
            conn = dataSource.getConnection();
            // Update tuple
            String q = "select * from request where id=?";
            prep = conn.prepareStatement(q);
            prep.setInt(1, id);

            ResultSet rs = prep.executeQuery(q);
            rs.next();
            request.setId(id);
            request.setRequesterId(rs.getInt("requester_id"));
            request.setAccountId(rs.getInt("account_id"));
            request.setApproverId(rs.getInt("approver_id"));
            request.setRequest_type(rs.getString("request_type"));
            request.setCurrent_value(rs.getString("current_value"));
            request.setRequested_value(rs.getString("requested_value"));
            request.setStatus(rs.getString("status"));
            request.setDescription(rs.getString("description"));

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                prep.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } // end of finally
        return request;
    }

    public Boolean removeByID(int id){
        boolean status = true;
        Request request = new Request();
        // set up SQL connection
        Connection conn = null;
        PreparedStatement prep = null;
        try {
            conn = dataSource.getConnection();
            // Update tuple
            String q = "delete from request where id=?";
            prep = conn.prepareStatement(q);
            prep.setInt(1, id);

            int row_changed = prep.executeUpdate();
            if(row_changed != 1) {
                status = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                prep.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } // end of finally
        return status;
    }

    public ArrayList<Request> getByRequesterID(int id){

        ArrayList<Request> requests = new ArrayList<Request>();
        // set up SQL connection
        Connection conn = null;
        PreparedStatement prep = null;
        try {
            conn = dataSource.getConnection();
            // Update tuple
            String q = "select * from request where requester_id=?";
            prep = conn.prepareStatement(q);
            prep.setInt(1, id);

            ResultSet rs = prep.executeQuery(q);
            while(rs.next()) {
                Request request = new Request();
                request.setId(id);
                request.setRequesterId(rs.getInt("requester_id"));
                request.setAccountId(rs.getInt("account_id"));
                request.setApproverId(rs.getInt("approver_id"));
                request.setRequest_type(rs.getString("request_type"));
                request.setCurrent_value(rs.getString("current_value"));
                request.setRequested_value(rs.getString("requested_value"));
                request.setStatus(rs.getString("status"));
                request.setDescription(rs.getString("description"));

                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                prep.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } // end of finally
        return requests;
    }


    /*
    Called by approveRequest() to change the value
    This Function assues it is a contact info being updated
    * */
    public Boolean update_contact_value(int requestID, String column, String newValue){
        boolean status = true;
        String table="Customer";
        // set up SQL connection
        Connection conn = null;
        PreparedStatement prep = null;
        try {
            conn = dataSource.getConnection();
            // Update tuple
            //TODO: See how to handle email string vs mobile no int
            String q = "update "+ table +" set "+ column +" = "+newValue+" where id =?";

            prep = conn.prepareStatement(q);
            //prep.setInt(1, newValue);
            prep.setInt(1, requestID);

            int row_changed = prep.executeUpdate();
            if(row_changed != 1) {
                status = false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            status = false;
        } catch (Exception e) {
            e.printStackTrace();
            status = false;

        } finally {
            try {
                prep.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } // end of finally

        return status;
    }


    /*
    Called by approveRequest() to change the value
    This Function assumes it is transaction info being approved
    * */
    public Boolean update_transaction(int requestID, String column, int newValue){
        boolean status = true;
        Request request=this.getByID(requestID);
        String account_table="Account";
        String account_column="account";
        // set up SQL connection
        Connection conn = null;
        PreparedStatement prep = null;
        try {
            conn = dataSource.getConnection();
            //Extract current amount
            //TODO: See how to handle transaction? has to be via transactions Service
            //TODO: Which column contains user account balance?
            String q = "Select "+account_column+" from "+ account_table +" where id =?";

            prep = conn.prepareStatement(q);
            //prep.setInt(1, newValue);
            prep.setInt(1, request.getId());


            ResultSet rs = prep.executeQuery(q);
            rs.next();
            int balance=rs.getInt(account_column);

            if(balance < newValue)
            {
                //Modify Account
            }
            else
            {
                //Decline Trannsaction
            }
        } catch (SQLException e) {
            e.printStackTrace();
            status = false;
        } catch (Exception e) {
            e.printStackTrace();
            status = false;

        } finally {
            try {
                prep.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } // end of finally

        return status;
    }

    public Boolean approveRequest(int req_id, int approver_id) {

        boolean status = true;
        // set up SQL connection
        Connection conn = null;
        PreparedStatement prep = null;
        try {
            conn = dataSource.getConnection();
            //Handle the request
            Request request=this.getByID(req_id);
            switch(request.getRequest_type())
            {
                case "Contact":
                    switch(request.getDescription())
                    {
                        case "Email":
                            update_contact_value(req_id,"email",request.getRequested_value());
                            break;
                        case "Mobile":
                            update_contact_value(req_id,"mobile",request.getRequested_value());
                            break;
                        default:
                    }
                    break;
                case "Transaction":
                    switch(request.getDescription())
                    {
                        case "BankAccount":
                            update_contact_value(req_id,"email",request.getRequested_value());
                            break;
                        case "Credit":
                            update_transaction(req_id,"mobile",Integer.parseInt(request.getRequested_value()));
                            break;
                        case "Debit":
                            update_transaction(req_id,"mobile",Integer.parseInt(request.getRequested_value()));
                            break;
                        default:
                    }
                    break;
            }

            // Update tuple
            String q = "UPDATE " + TABLE_NAME + " SET approver_id=?, status=?, timestamp_updated=? WHERE id=?";
            prep = conn.prepareStatement(q);
            prep.setInt(1, approver_id);
            prep.setString(2, "approved");
            Date date = new Date();
            Timestamp ts = new Timestamp(date.getTime());
            prep.setTimestamp(3, ts);

            int row_changed = prep.executeUpdate();
            if(row_changed != 1) {
                status = false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            status = false;
        } catch (Exception e) {
            e.printStackTrace();
            status = false;

        } finally {
            try {
                prep.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } // end of finally

        // TODO: perform transaction here??

        return status;

    }

    public Boolean rejectRequest (int req_id, int approver_id) {
        boolean status = true;

        // set up SQL connection
        Connection conn = null;
        PreparedStatement prep = null;
        try {
            conn = dataSource.getConnection();
            // Update tuple
            String q = "UPDATE " + TABLE_NAME + " SET approver_id=?, status=?, timestamp_updated=? WHERE id=?";
            prep = conn.prepareStatement(q);
            prep.setInt(1, approver_id);
            prep.setString(2, "rejected");
            Date date = new Date();
            Timestamp ts = new Timestamp(date.getTime());
            prep.setTimestamp(3, ts);

            int row_changed = prep.executeUpdate();
            if(row_changed != 1) {
                status = false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            status = false;
        } catch (Exception e) {
            e.printStackTrace();
            status = false;

        } finally {
            try {
                prep.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } // end of finally

        return status;
    }
}
