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

    public Boolean delete(Request request){
        boolean status = true;

        try {
            // Update tuple
            requestRepository.delete(request);
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

    public Boolean save(Request request){
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
            String sql = "select r from "+ Request.class.getName() +" r where r.approver_id=:approver_id";
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
}