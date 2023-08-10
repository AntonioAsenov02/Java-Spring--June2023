package orm;

import orm.annotations.Column;
import orm.annotations.Entity;
import orm.annotations.Id;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityManager<E> implements DbContext<E> {
    private final Connection connection;

    public EntityManager(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean persist(E entity) throws SQLException, IllegalAccessException {

        String tableName = this.getTableName(entity.getClass());
        String fieldList = getDbFieldsWithoutIdentity(entity);
        String valueList = this.getInsertValues(entity);

        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, fieldList,valueList);

        return this.connection.prepareStatement(sql).execute();
    }

    @Override
    public Iterable<E> find(Class<E> entityType) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return find(entityType, null);
    }

    @Override
    public Iterable<E> find(Class<E> entityType, String where) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        String tableName = this.getTableName(entityType);

        String sql = String.format("SELECT * FROM %s %s",entityType,
                where == null ? "" : "WHERE " + where);

        ResultSet resultSet = this.connection.prepareStatement(sql).executeQuery();

        List<E> result = new ArrayList<>();

        E entity = this.createEntity(entityType, resultSet);

        while (entity != null){

            result.add(entity);
            entity = this.createEntity(entityType, resultSet);
        }

        return result;
    }

    @Override
    public E findFirst(Class<E> entityType) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return findFirst(entityType,null);
    }

    @Override
    public E findFirst(Class<E> entityType, String where) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        String tableName = this.getTableName(entityType);

        String sql = String.format("SELECT * FROM %s %s LIMIT 1",entityType,
                where == null ? "" : "WHERE " + where);

        ResultSet result = this.connection.prepareStatement(sql).executeQuery();

        return this.createEntity(entityType, result);
    }

    private E createEntity(Class<E> entityType, ResultSet result) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        if (!result.next()) {
            return null;
        }

        E entity = entityType.getDeclaredConstructor().newInstance();

        Field[] declaredFields = entityType.getDeclaredFields();

        for (Field declaredField : declaredFields) {

            if (declaredField.isAnnotationPresent(Column.class)) {
                String fieldName = declaredField.getAnnotation(Column.class).name();
                String value = result.getString(fieldName);
                this.fillData(entity,declaredField,value);

            }else if (declaredField.isAnnotationPresent(Id.class)) {
                String fieldName = declaredField.getName();
                String value = result.getString(fieldName);
                this.fillData(entity, declaredField, value);
            }
        }

        return entity;
    }

    private void fillData(E entity, Field field, String value) throws IllegalAccessException {

        if (field.getType() == long.class || field.getType() == Long.class) {
            field.setLong(entity, Long.parseLong(value));
        }else if (field.getType() == int.class || field.getType() == Integer.class) {
            field.setInt(entity, Integer.parseInt(value));
        }else if (field.getType() == LocalDate.class) {
            field.set(entity, LocalDate.parse(value));
        }else if (field.getType() == String.class) {
            field.set(entity, value);
        }else {
            throw new ORMException("Unsupported type " + field.getType());
        }
    }

    private String getTableName(Class<?> clazz) {

        Entity annotation = clazz.getAnnotation(Entity.class);

        if (annotation == null){

            throw new ORMException("Provided class does not have Entity annotation");
        }

        return annotation.name();
    }

    private String getDbFieldsWithoutIdentity(E entity) {

        return Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(f -> f.getAnnotation(Column.class) != null)
                .map(f -> f.getAnnotation(Column.class).name())
                .collect(Collectors.joining(","));
    }

    private String getInsertValues(E entity) throws IllegalAccessException {

        Field [] declaredFields = entity.getClass().getDeclaredFields();

        List<String> result = new ArrayList<>();

        for (Field declaredField : declaredFields) {

            if (declaredField.getAnnotation(Column.class) == null) {
                continue;
            }

            declaredField.setAccessible(true);
            Object value = declaredField.get(entity);
            result.add("\"" + value.toString() + "\"");

        }

      return String.join(",", result);
    }
}
