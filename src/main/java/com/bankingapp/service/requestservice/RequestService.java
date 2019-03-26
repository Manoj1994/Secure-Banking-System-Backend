package com.bankingapp.service.requestservice;

import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.sql.DataSource;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import com.bankingapp.model.account.CreditCard;
import com.bankingapp.model.account.Customer;
import com.bankingapp.model.account.DebitCard;
import com.bankingapp.model.request.Request;
import com.bankingapp.repository.requestrepository.RequestRepository;
import com.bankingapp.service.accountservice.AccountUpdateService;
import com.bankingapp.service.accountservice.CreditCardService;
import com.bankingapp.service.accountservice.DebitCardService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;

@Component
@Transactional
public class RequestService {

    @Autowired
    EntityManager entityManager;

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    CreditCardService creditCardService;

    @Autowired
    DebitCardService debitCardService;

    @Autowired
    AccountUpdateService accountUpdateService;


    private final String TABLE_NAME = "request"; // change this according to your request table name
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate; // so far I haven't use this yet - Waris

    /*
    Assumes the status o New Request is always Pending at first
     */
    public Boolean add_new_request(Request request){
        boolean status = true;

        try {
            // Update tuple
            requestRepository.save(request);
            return true;

        } catch (NoResultException e) {
            e.printStackTrace();
            status = false;
        } catch (Exception e) {
            e.printStackTrace();
            status = false;
        }
        return status;
    }

    /*
    Assumes The request id is unique
    */
    public Request getByID(int id){

        Request request = new Request();
        // set up SQL connection
        try {
            // Update tuple
            String sql = "select r from "+Request.class.getName()+" r where r.id=:id";
            Query query = entityManager.createQuery(sql, Request.class);
            query.setParameter("id", id);

            List<Request> rs = query.getResultList();
            request = rs.get(0);
        } catch (NoResultException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }

    public Boolean removeByID(int id){
        boolean status = true;
        Request request = new Request();
        // set up SQL connection
        try {
            // Update tuple
            requestRepository.deleteById((long)id);
        } catch (NoResultException e) {
            status=false;
        } catch (Exception e) {
            e.printStackTrace();
            status=false;
        }
        return status;
    }

    public List<Request> getByAllRequest(){

        List<Request> requests=null;
        // set up SQL connection
        try {
            // Update tuple
            String sql = "select r from "+ Request.class.getName() +" r order by r.id";
            Query query = entityManager.createQuery(sql, Request.class);
            requests = query.getResultList();

            System.out.println("The list of requests collected s"+requests.size());
        } catch (NoResultException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requests;
    }

    public List<Request> getByRequesterID(int id){

        List<Request> requests = null;
        // set up SQL connection
        try {
            // Update tuple
            String sql = "select r from "+ Request.class.getName() +" r where requester_id=:requester_id";
            Query query = entityManager.createQuery(sql, Request.class);
            query.setParameter("requester_id",id);

            requests = query.getResultList();

        } catch (NoResultException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requests;
    }

    public List<Request> getByApproverID(int id){

        List<Request> requests = null;
        // set up SQL connection
        try {
            // Update tuple
            String sql = "select r from "+ Request.class.getName() +" r where approver_id=:approver_id";
            Query query = entityManager.createQuery(sql, Request.class);
            query.setParameter("approver_id",id);

            requests = query.getResultList();

        } catch (NoResultException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requests;
    }


    /*
    Called by approveRequest() to change the value
    This Function assues it is a contact info being updated
    * */
    public Boolean update_contact_value(int cus_id, String column, String newValue){
        boolean status = true;
        String table="Customer";
        // set up SQL connection
        try {
            // Update tuple
            String sql = "select c from "+ Customer.class.getName() +" c where c.user_id =:id";

            Query query = entityManager.createQuery(sql, Customer.class);
            query.setParameter("id", cus_id);

            List<Customer> customers = query.getResultList();
            Customer customer = customers.get(0);

            switch(column)
            {
                case "Email":
                    customer.setEmail(newValue);
                    break;
                case "Mobile":
                    customer.setContact(newValue);
                    break;
                case "Name":
                    customer.setName(newValue);
                    break;
                default:
                    return false;
            }

        } catch (NoResultException e) {
            e.printStackTrace();
            status = false;
        } catch (Exception e) {
            e.printStackTrace();
            status = false;

        }
        return status;
    }


    public Boolean approveRequest(int req_id, int approver_id) {

        boolean status = true;
        try {
            //Handle the request
            Request request=this.getByID(req_id);
            switch(request.getRequest_type())
            {
                case "Update":
                    int cus_id=request.getRequesterId();
                    status=update_contact_value(cus_id,request.getDescription(),request.getRequested_value());
                    break;
                case "Create":
                    int req_val = Integer.parseInt(request.getRequested_value());
                    int request_id = request.getRequesterId();
                    switch(request.getDescription())
                    {
                        case "Credit Card":
                            //req+val =account id
                            CreditCard creditCard=new CreditCard();
                            creditCard.setAccount_no(req_val);
                            //TODO:card limit hardcoded
                            creditCard.setCard_limit(750);
                            //status= creditCardService.addNewCreditCard(creditCard);
                            break;
                        case "Debit Card":
                            //req+val =account id
                            DebitCard debitCard=new DebitCard();
                            debitCard.setAccount_no(req_val);
                            //status= debitCardService.AddNewDebitCard(debitCard);
                            break;
                        case "Current Account":
                            //Requester id =customer id
                            AccountUpdateService acc1=new AccountUpdateService();
                            status=acc1.createAccount(request_id,2);
                            break;
                        case "Savings Account":
                            AccountUpdateService acc2=new AccountUpdateService();
                            status=acc2.createAccount(request_id,1);
                            break;
                        default:
                            return false;
                    }
                    break;
                case "Delete":
                    String requested_val = request.getRequested_value();
                    switch(request.getDescription())
                    {
                        case "Credit Card":{
                            //req+val =Card id
                            long card_no=Long.parseLong(requested_val);
                            //creditCardService.deleteCreditCard(card_no);
                            break;
                        }
                        case "Debit Card": {
                            //req+val =Card id
                            long card_no = Long.parseLong(requested_val);
                            //debitCardService.deleteDebitCard(card_no);
                            break;
                        }
                        case "Current Account":
                        case "Savings Account": {
                            //req_val = account -id
                            int account_id=Integer.parseInt(requested_val);
                            status = accountUpdateService.deleteAccount(account_id);
                            break;
                        }
                        default:
                            return false;
                    }
                    break;
                default:

            }

            // Update tuple
            Timestamp createdDateTime = new Timestamp(new java.util.Date().getTime());
            request.setStatus("Approved");
            request.setTimestamp_updated(createdDateTime);
            requestRepository.save(request);

        } catch (NoResultException e) {
            e.printStackTrace();
            status = false;
        } catch (Exception e) {
            e.printStackTrace();
            status = false;
        }

        return status;

    }

    public Boolean rejectRequest (int req_id, int approver_id) {
        boolean status = true;

        // set up SQL connection
        try {
            // Update tuple
            Timestamp createdDateTime = new Timestamp(new java.util.Date().getTime());

            //String sql = "UPDATE " + Request.class.getName() + " r SET r.approver_id=:approver_id, r.status=:status, r.timestamp_updated=:time WHERE r.id=:id";
            String sql = "Select r from " + Request.class.getName() + " r WHERE r.id=:id";

            Query query = entityManager.createQuery(sql, Request.class);
            query.setParameter("id",req_id);

            List<Request> rs = query.getResultList();
            Request request = rs.get(0);
            request.setStatus("Rejected");
            request.setTimestamp_updated(createdDateTime);
            requestRepository.save(request);
        } catch (NoResultException e) {
            e.printStackTrace();
            status = false;
        } catch (Exception e) {
            e.printStackTrace();
            status = false;
        }
        return status;
    }
}