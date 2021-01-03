package foodwarehouse.database.jdbc;

import foodwarehouse.core.data.address.Address;
import foodwarehouse.core.data.employee.Employee;
import foodwarehouse.core.data.storage.Storage;
import foodwarehouse.core.data.storage.StorageRepository;
import foodwarehouse.database.rowmappers.StorageResultSetMapper;
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
public class JdbcStorageRepository implements StorageRepository {

    private final String procedureInsertStorage = "CALL `INSERT_STORAGE`(?,?,?,?,?,?)";

    private final String procedureUpdateStorage = "CALL `UPDATE_STORAGE`(?,?,?,?)";

    private final String procedureDeleteStorage = "CALL `DELETE_STORAGE`(?)";

    private final String procedureReadStorages = "CALL `GET_STORAGES`()";
    private final String procedureReadStorageById = "CALL `GET_STORAGE_BY_ID`(?)";

    private final Connection connection;

    @Autowired
    public JdbcStorageRepository(DataSource dataSource) throws SQLException {
        this.connection = dataSource.getConnection();
    }


    @Override
    public Optional<Storage> insertStorage(Address address, Employee manager, String name, int capacity, boolean isColdStorage) throws SQLException {
        CallableStatement callableStatement = connection.prepareCall(procedureInsertStorage);
        callableStatement.setInt(1, address.addressId());
        callableStatement.setInt(2, manager.employeeId());
        callableStatement.setString(3, name);
        callableStatement.setInt(4, capacity);
        callableStatement.setBoolean(5, isColdStorage);

        callableStatement.executeQuery();
        int storageId = callableStatement.getInt(6);

        return Optional.of(new Storage(storageId, address, manager, name, capacity, isColdStorage));
    }

    @Override
    public Optional<Storage> updateStorage(int storageId, Address address, Employee manager, String name, int capacity, boolean isColdStorage) throws SQLException {
        CallableStatement callableStatement = connection.prepareCall(procedureUpdateStorage);
        callableStatement.setInt(1, storageId);
        callableStatement.setString(2, name);
        callableStatement.setInt(3, capacity);
        callableStatement.setBoolean(4, isColdStorage);

        callableStatement.executeQuery();
        return Optional.of(new Storage(storageId, address, manager, name, capacity, isColdStorage));
    }

    @Override
    public boolean deleteStorage(int storageId) throws SQLException {
        CallableStatement callableStatement = connection.prepareCall(procedureDeleteStorage);
        callableStatement.setInt(1, storageId);

        callableStatement.executeQuery();
        return true;
    }

    @Override
    public List<Storage> findAllStorages() throws SQLException {
        CallableStatement callableStatement = connection.prepareCall(procedureReadStorages);
        ResultSet resultSet = callableStatement.executeQuery();
        List<Storage> storages = new LinkedList<>();
        while(resultSet.next()) {
            storages.add(new StorageResultSetMapper().resultSetMap(resultSet));
        }
        return storages;
    }

    @Override
    public Optional<Storage> findStorage(int storageId) throws SQLException {
        CallableStatement callableStatement = connection.prepareCall(procedureReadStorageById);
        callableStatement.setInt(1, storageId);

        ResultSet resultSet = callableStatement.executeQuery();
        return Optional.ofNullable(new StorageResultSetMapper().resultSetMap(resultSet));
    }
}
