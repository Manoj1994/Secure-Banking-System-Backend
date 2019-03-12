//package com.bankingapp.service.accountservice;
//
//import com.bankingapp.model.account.Customer;
//import com.bankingapp.model.account.SavingsAccount;
//import com.bankingapp.model.transaction.Transaction;
//import org.springframework.stereotype.Component;
//
//import java.sql.SQLException;
//
//@Component
//public class AccountUpdateService {
//
//    public boolean addMoney(Customer user, SavingsAccount account, Transaction transaction, Double amount, String accountType) throws SQLException
//    {
//        boolean status=false;
//        java.sql.Timestamp createdDateTime = new java.sql.Timestamp(new java.util.Date().getTime());
//        String query_AddToPending = "INSERT INTO transaction (payer_id, payee_id,amount, hashvalue,transaction_type,description,status,approver,critical,timestamp_created,timestamp_updated) values (?,?,?,?,?,?,?,?,?,?,?)";
//        try
//        {
//            con = dataSource.getConnection();
//            con.setAutoCommit(false);
//            ps = con.prepareStatement(query_AddToPending,Statement.RETURN_GENERATED_KEYS);
//            ps.setInt(1, account.getAccount_number());
//            ps.setInt(2, account.getAccount_number());
//            ps.setBigDecimal(3, amount);
//            ps.setString(4,"hasvalue");
//            ps.setString(5,"creditfunds");
//            ps.setString(6,"Deposit money to account");
//            ps.setString(7,"pending");
//            ps.setString(8,"External user");
//            ps.setInt(9, 0);
//            ps.setTimestamp(10,createdDateTime);
//            ps.setTimestamp(11,createdDateTime);
//            int out =ps.executeUpdate();
//            System.out.println("first query");
//            if (out != 0) {
//                ResultSet rs = ps.getGeneratedKeys();
//                rs.next();
//                System.out.println("ID----" + rs.getInt(1));
//                transaction.setId(rs.getInt(1));
//            }
//
//            con.commit();
//            status =true;
//
//        }catch(SQLException e){
//            con.rollback();
//            status = false;
//            e.printStackTrace();
//        }finally{
//            try {
//                ps.close();
//                con.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        return status;
//    }
//
//    public boolean withdrawMoney(Customer user,BankAccountDB account,Transaction transaction,BigDecimal amount,String accountType) throws SQLException
//    {
//        boolean status=false;
//        Connection con = null;
//        PreparedStatement ps = null;
//        java.sql.Timestamp createdDateTime = new java.sql.Timestamp(new java.util.Date().getTime());
//        String query_addBalance ="update bank_accounts set hold = hold + "+amount+" WHERE external_users_id="+user.getId()+" and account_type='"+accountType+"'";
//        String query_AddToPending = "INSERT INTO transaction_pending (payer_id, payee_id,amount, hashvalue,transaction_type,description,status,approver,critical,timestamp_created,timestamp_updated) values (?,?,?,?,?,?,?,?,?,?,?)";
//        try
//        {
//            con = dataSource.getConnection();
//            con.setAutoCommit(false);
//            ps = con.prepareStatement(query_AddToPending,Statement.RETURN_GENERATED_KEYS);
//            ps.setInt(1, account.getAccount_number());
//            ps.setInt(2, account.getAccount_number());
//            ps.setBigDecimal(3, amount);
//            ps.setString(4,"hasvalue");
//            ps.setString(5,"debitfunds");
//            ps.setString(6,"Withdrawal of money from account");
//            ps.setString(7,"pending");
//            ps.setString(8,"External user");
//            ps.setInt(9, 0);
//            ps.setTimestamp(10,createdDateTime);
//            ps.setTimestamp(11,createdDateTime);
//            int out =ps.executeUpdate();
//            System.out.println("first query");
//            if (out != 0) {
//                ResultSet rs = ps.getGeneratedKeys();
//                rs.next();
//                System.out.println("ID----" + rs.getInt(1));
//                transaction.setId(rs.getInt(1));
//            }
//            ps.close();
//            ps = con.prepareStatement(query_addBalance);
//            ps.executeUpdate();
//            con.commit();
//
//            status =true;
//        }catch(SQLException e){
//            con.rollback();
//            status = false;
//            e.printStackTrace();
//        }finally{
//            try {
//                ps.close();
//                con.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        return status;
//    }
//}
