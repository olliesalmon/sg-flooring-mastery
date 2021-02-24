package com.sg.ollie.dao;

import com.sg.ollie.dto.Order;
import org.springframework.stereotype.Component;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class OrderFileDaoImpl implements OrderFileDao {
    public final String ORDER_DIRECTORY;
    public final String BACKUP_DIRECTORY;
    public final String ORDER_DAYS_FILE_DIRECTORY;
    public final String BACKUP_FILE;
    public final String ORDER_DAYS_FILE;

    public static final String DELIMITER = ",";
    private final Map<Integer, Order> orders = new HashMap<>();
    private final List<String> orderDays = new ArrayList<>();

    public OrderFileDaoImpl() {
        ORDER_DIRECTORY = "Orders/";
        BACKUP_DIRECTORY = "Backup/";
        ORDER_DAYS_FILE_DIRECTORY = "Data/";
        ORDER_DAYS_FILE = ORDER_DAYS_FILE_DIRECTORY + "OrderDays.txt";
        BACKUP_FILE = BACKUP_DIRECTORY + "DataExport.txt";
    }

    public OrderFileDaoImpl(String orderDirectory, String backupDirectory, String orderDaysFileDirectory,
                            String backupFile, String daysContainingOrdersFile) {
        ORDER_DIRECTORY = orderDirectory;
        BACKUP_DIRECTORY = backupDirectory;
        ORDER_DAYS_FILE_DIRECTORY = orderDaysFileDirectory;
        ORDER_DAYS_FILE = ORDER_DAYS_FILE_DIRECTORY + daysContainingOrdersFile;
        BACKUP_FILE = BACKUP_DIRECTORY + backupFile;

    }

    @Override
    public boolean checkOrderExistsOnDate(String orderDate) throws NonExistentOrderException {
        if (orderDays.contains(orderDate)) {
            return true;
        }
        else return false;
    }

    @Override
    public Order addOrder(Order orderToAdd, String orderDate) throws FilePersistenceException {
        String orderFileName = ORDER_DIRECTORY + "Orders_" + orderDate + ".txt";
        if (orderDays.contains(orderDate)) {
            loadOrderFile(orderFileName);
        }
        else {
            orderDays.add(orderDate);
        }
        orders.put(orderToAdd.getOrderNo(), orderToAdd);
        writeOrderFile(orderFileName);
        writeOrderDays();
        orders.clear();
        return orderToAdd;
    }

    @Override
    public Order calculateAdditionalOrderFields(Order orderToCalc, String orderDate) throws NonExistentOrderException, FilePersistenceException {
        orderToCalc.calculateMaterialCost();
        orderToCalc.calculateLabourCost();
        orderToCalc.calculateTax();
        orderToCalc.calculateTotal();
        return orderToCalc;
    }

    @Override
    public Order calculateNextOrderNo(Order orderToCalc, String orderDate) throws FilePersistenceException, NonExistentOrderException {
        String orderFileName = ORDER_DIRECTORY + "Orders_" + orderDate + ".txt";
        if (checkOrderExistsOnDate(orderDate)) {
            loadOrderFile(orderFileName);
            OptionalInt orderNo = orders.keySet()
                    .stream()
                    .mapToInt(i -> i)
                    .max();
            orderToCalc.setOrderNo(orderNo.getAsInt() + 1);
        }
        else {
            orderToCalc.setOrderNo(1);
        }
        return orderToCalc;
    }

    @Override
    public Order getOrder(int orderNo, String orderDate) throws FilePersistenceException {
        String orderFileName = ORDER_DIRECTORY + "Orders_" + orderDate + ".txt";
        loadOrderFile(orderFileName);
        return orders.get(orderNo);
    }

    @Override
    public List<Order> getAllOrders(String orderDate) throws FilePersistenceException {
        String orderFileName = ORDER_DIRECTORY + "Orders_" + orderDate + ".txt";
        loadOrderFile(orderFileName);
        return new ArrayList<>(orders.values());
    }

    @Override
    public Order editOrder(Order newOrder, String orderDate) throws FilePersistenceException {
        String orderFileName = ORDER_DIRECTORY + "Orders_" + orderDate + ".txt";
        loadOrderFile(orderFileName);
        newOrder.setOrderNo(newOrder.getOrderNo());
        orders.put(newOrder.getOrderNo(), newOrder);
        writeOrderFile(orderFileName);
        orders.clear();
        return newOrder;
    }

    @Override
    public Order removeOrder(int orderNo, String orderDate) throws FilePersistenceException {
        String orderFileName = ORDER_DIRECTORY + "Orders_" + orderDate + ".txt";

        loadOrderFile(orderFileName);
        Order removedOrder = orders.remove(orderNo);

        File file = new File(orderFileName);
        if (orders.size() == 0) {
            orderDays.remove(orderDate);
            file.delete();
            writeOrderDays();
        }
        else writeOrderFile(orderFileName);
        orders.clear();
        return removedOrder;
    }

    @Override
    public void writeBackup() throws FilePersistenceException {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileWriter(BACKUP_FILE));
            for (String currentDay : orderDays) {
                String orderFileName = ORDER_DIRECTORY + "Orders_" + currentDay + ".txt";
                loadOrderFile(orderFileName);
                for (Order currentOrder : orders.values()) {
                    String orderAsString = marshallOrder(currentOrder);
                    String currentDateString = LocalDate.parse(currentDay, DateTimeFormatter.ofPattern("MMdduuuu"))
                            .format(DateTimeFormatter.ofPattern("MM/dd/uuuu"));
                    out.println(orderAsString + DELIMITER + currentDateString);
                    out.flush();
                }
            }
        } catch (IOException e) {
            throw new FilePersistenceException("Could not write order data to file");
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public String marshallOrder(Order orderToMarshall) {
        return orderToMarshall.getOrderNo() + DELIMITER + orderToMarshall.getCustomerName() + DELIMITER +
                orderToMarshall.getState() + DELIMITER + orderToMarshall.getTaxRate() + DELIMITER +
                orderToMarshall.getProductType() + DELIMITER + orderToMarshall.getArea() + DELIMITER +
                orderToMarshall.getCostPerSquareFoot() + DELIMITER + orderToMarshall.getLabourCostPerSquareFoot() +
                DELIMITER + orderToMarshall.getMaterialCost() + DELIMITER + orderToMarshall.getLabourCost() +
                DELIMITER + orderToMarshall.getTax() + DELIMITER + orderToMarshall.getTotal();
    }

    public Order unmarshallOrder(String orderAsString) {
        Object[] splitString = orderAsString.split(DELIMITER);
        Order splitOrder = new Order();
        splitOrder.setOrderNo((Integer.parseInt((String) splitString[0])));
        splitOrder.setCustomerName((String) splitString[1]);
        splitOrder.setState((String) splitString[2]);
        splitOrder.setTaxRate(new BigDecimal((String) splitString[3]));
        splitOrder.setProductType((String) splitString[4]);
        splitOrder.setArea(new BigDecimal((String) splitString[5]));
        splitOrder.setCostPerSquareFoot(new BigDecimal((String) splitString[6]));
        splitOrder.setLabourCostPerSquareFoot(new BigDecimal((String) splitString[7]));
        splitOrder.setMaterialCost(new BigDecimal((String) splitString[8]));
        splitOrder.setLabourCost(new BigDecimal((String) splitString[9]));
        splitOrder.setTax(new BigDecimal((String) splitString[10]));
        splitOrder.setTotal(new BigDecimal((String) splitString[11]));

        return splitOrder;
    }

    public void loadOrderFile(String orderFileName) throws FilePersistenceException {
        orders.clear();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(orderFileName)));
            while (scanner.hasNextLine()) {
                String currentLine = scanner.nextLine();
                Order currentOrder = unmarshallOrder(currentLine);
                orders.put(currentOrder.getOrderNo(), currentOrder);
            }
        } catch (FileNotFoundException e) {
            throw new FilePersistenceException("There is no order with the information you have specified");
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    public void writeOrderFile(String orderFileName) throws FilePersistenceException {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileWriter(orderFileName));
            for (Order currentOrder : orders.values()) {
                String orderAsString = marshallOrder(currentOrder);
                out.println(orderAsString);
                out.flush();
            }
        } catch (IOException e) {
            throw new FilePersistenceException("Could not write order data to file");
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    @Override
    public void loadOrderDays() throws FilePersistenceException {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(ORDER_DAYS_FILE)));
            while (scanner.hasNextLine()) {
                String currentLine = scanner.nextLine();
                orderDays.add(currentLine);
            }
        } catch (IOException e) {
            throw new FilePersistenceException("Could not load order days from file");
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    public void writeOrderDays() throws FilePersistenceException {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileWriter(ORDER_DAYS_FILE));
            for (String orderDay : orderDays) {
                out.println(orderDay);
                out.flush();
            }
        } catch (IOException e) {
            throw new FilePersistenceException("Could not write order days to file");
        } finally {
            if (out != null) {
                out.close();
            }
            orders.clear();
        }
    }
}