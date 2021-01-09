package foodwarehouse.database.jdbc;

import foodwarehouse.core.data.customer.Customer;
import foodwarehouse.core.data.delivery.Delivery;
import foodwarehouse.core.data.order.Order;
import foodwarehouse.core.data.order.OrderRepository;
import foodwarehouse.core.data.order.OrderState;
import foodwarehouse.core.data.payment.Payment;
import foodwarehouse.database.rowmappers.DeliveryResultSetMapper;
import foodwarehouse.database.rowmappers.OrderResultSetMapper;
import foodwarehouse.database.tables.CarTable;
import foodwarehouse.database.tables.DeliveryTable;
import foodwarehouse.database.tables.OrderTable;
import foodwarehouse.web.error.RestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcOrderRepository implements OrderRepository {

    private final Connection connection;

    @Autowired
    JdbcOrderRepository(DataSource dataSource) {
        try {
            this.connection = dataSource.getConnection();
        }
        catch(SQLException sqlException) {
            throw new RestException("Cannot connect to database!");
        }
    }

    @Override
    public Optional<Order> createOrder(Payment payment, Customer customer, Delivery delivery, String comment) {
        try {
            CallableStatement callableStatement = connection.prepareCall(OrderTable.Procedures.INSERT);
            callableStatement.setInt(1, payment.paymentId());
            callableStatement.setInt(2, customer.customerId());
            callableStatement.setInt(3, delivery.deliveryId());
            callableStatement.setString(4, comment);

            callableStatement.executeQuery();
            int orderId = callableStatement.getInt(5);
            return Optional.of(new Order(orderId, payment, customer, delivery, comment, OrderState.PENDING));
        }
        catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Order> updateOrderState(
            int orderId,
            Payment payment,
            Customer customer,
            Delivery delivery,
            String comment,
            OrderState orderState) {
        try {
            CallableStatement callableStatement = connection.prepareCall(OrderTable.Procedures.UPDATE_STATE);
            callableStatement.setInt(1, orderId);
            callableStatement.setString(2, orderState.value());

            callableStatement.executeQuery();

            return Optional.of(new Order(orderId, payment, customer, delivery, comment, orderState));
        }
        catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Order> updateOrderPayment(
            int orderId,
            Payment payment,
            Customer customer,
            Delivery delivery,
            String comment,
            OrderState orderState) {
        try {
            CallableStatement callableStatement = connection.prepareCall(OrderTable.Procedures.UPDATE_PAYMENT);
            callableStatement.setInt(1, orderId);
            callableStatement.setInt(2, payment.paymentId());

            callableStatement.executeQuery();

            return Optional.of(new Order(orderId, payment, customer, delivery, comment, orderState));
        }
        catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteOrder(int orderId) {
        try {
            CallableStatement callableStatement = connection.prepareCall(OrderTable.Procedures.DELETE);
            callableStatement.setInt(1, orderId);

            callableStatement.executeQuery();
            return true;
        }
        catch (SQLException sqlException) {
            return false;
        }
    }

    @Override
    public Optional<Order> findOrderById(int orderId) {
        try {
            CallableStatement callableStatement = connection.prepareCall(OrderTable.Procedures.READ_BY_ID);
            callableStatement.setInt(1, orderId);

            ResultSet resultSet = callableStatement.executeQuery();
            Order order = null;
            if(resultSet.next()) {
                order = new OrderResultSetMapper().resultSetMap(resultSet, "");
            }
            return Optional.ofNullable(order);
        }
        catch (SQLException sqlException) {
            return Optional.empty();
        }
    }

    @Override
    public List<Order> findOrders() {
        List<Order> orders = new LinkedList<>();
        try {
            CallableStatement callableStatement = connection.prepareCall(CarTable.Procedures.READ_ALL);

            ResultSet resultSet = callableStatement.executeQuery();
            while(resultSet.next()) {
                orders.add(new OrderResultSetMapper().resultSetMap(resultSet, ""));
            }
        }
        catch(SQLException sqlException) {
            sqlException.getMessage();
        }
        return orders;
    }
}
