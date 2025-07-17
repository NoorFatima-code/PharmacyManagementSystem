package pharmacymanagmentsystem;

import java.util.*;

public class Order_Manager {

    private List<Order> orderList;
    private List<Supplier> supplierList;
    private List<Medicine> medicineList; // Still needed for console's internal logic
    private MedicineManager medicineManager; // Correctly injected and used

    private static Scanner sc = new Scanner(System.in); // Used only for console menu

    // Corrected Constructor: Now accepts MedicineManager
    public Order_Manager(List<Order> orderList, List<Medicine> medicineList, List<Supplier> supplierList, MedicineManager medicineManager) {
        this.orderList = orderList;
        this.medicineList = medicineList; // Initialize internal medicineList for console operations
        this.supplierList = supplierList; // Initialize internal supplierList for console operations
        this.medicineManager = medicineManager; // CRUCIAL FIX: Assign medicineManager
    }

    // ==== Menu (if still using console) ====
    public void showOrderMenu(String processedBy) { // Renamed parameter to processedBy for consistency
        int choice;
        do {
            System.out.println("\n=== Order Management Menu ===");
            System.out.println("1. Place New Order (Console Mode - Basic)");
            System.out.println("2. View All Orders");
            System.out.println("3. Approve Order (Received)");
            System.out.println("4. Delete Order");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> placeOrder(processedBy); // This is the old console way, GUI will use placeFinalOrder
                case 2 -> viewAllOrders();
                case 3 -> {
                    System.out.print("Enter Order ID to receive: ");
                    String receiveID = sc.nextLine();
                    boolean received = receiveOrder(receiveID);
                    System.out.println(received ? "Order marked as 'Received' and stock updated."
                                                 : "Failed to receive order (either not found or already received).");
                }
                case 4 -> {
                    System.out.print("Enter Order ID to delete: ");
                    String orderID = sc.nextLine();
                    boolean deleted = deleteOrder(orderID);
                    System.out.println(deleted ? "Order deleted successfully."
                                                 : "Failed to delete order (either not found or not in 'Pending' status).");
                }
                case 5 -> System.out.println("Returning to Main Menu...");
                default -> System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 5);
    }

    // ==== Place Order (Console version - for reference, GUI uses placeFinalOrder) ====
    public void placeOrder(String processedBy) { // Renamed parameter for consistency
        String orderID = generateOrderID(); // Using in-memory generation

        System.out.println("\nAvailable Suppliers:");
        if (supplierList.isEmpty()) {
            System.out.println("No suppliers available. Please add suppliers first.");
            return;
        }
        supplierList.forEach(s -> System.out.println("- " + s.getName() + " (ID: " + s.getSupplierID() + ")"));

        System.out.print("Enter supplier name or ID: ");
        String supplierInput = sc.nextLine().trim();

        Supplier selectedSupplier = supplierList.stream()
                .filter(s -> s.getName().equalsIgnoreCase(supplierInput) || s.getSupplierID().equalsIgnoreCase(supplierInput))
                .findFirst()
                .orElse(null);

        if (selectedSupplier == null) {
            System.out.println("Supplier not found.");
            return;
        }

        List<OrderItem> orderItems = new ArrayList<>();
        boolean addingItems = true;
        while (addingItems) {
            System.out.println("\nAvailable Medicines:");
            if (medicineList.isEmpty()) {
                System.out.println("No medicines available. Cannot add items to order.");
                break;
            }
            medicineList.stream()
//                    .sorted(Comparator.comparingInt(Medicine::getStock)) // Corrected to getStock()
                    .forEach(med -> {
                        // Assuming checkReorderStatus is on Medicine class
                        String stockStatus = med.getStockQuantity()== 0 ? "❌ Out of Stock" // Corrected to getStock()
                                : med.checkReorderStatus() ? "⚠️ Low Stock" // Assuming this method exists
                                : "✅ In Stock";
                        System.out.printf("- %s (ID: %s, Price: %.2f, Stock: %d) [%s]%n",
                                med.getName(), med.getMedicineID(), med.getPrice(), med.getStockQuantity(), stockStatus); // Corrected to getStock()
                    });

            System.out.print("Enter medicine name or ID to add (or 'done' to finish): ");
            String medInput = sc.nextLine().trim();

            if (medInput.equalsIgnoreCase("done")) break;

            Medicine selectedMed = medicineList.stream()
                    .filter(m -> m.getName().equalsIgnoreCase(medInput) || m.getMedicineID().equalsIgnoreCase(medInput))
                    .findFirst()
                    .orElse(null);

            if (selectedMed == null) {
                System.out.println("Medicine not found.");
                continue;
            }

            System.out.print("Enter quantity: ");
            int qty;
            try {
                qty = sc.nextInt();
                if (qty <= 0) {
                    System.out.println("Quantity must be positive.");
                    sc.nextLine(); // consume invalid input
                    continue;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid quantity. Please enter a number.");
                sc.nextLine(); // consume invalid input
                continue;
            }
            sc.nextLine(); // consume newline

            orderItems.add(new OrderItem(selectedMed, qty));
            System.out.println("Added " + qty + " of " + selectedMed.getName());
        }

        if (orderItems.isEmpty()) {
            System.out.println("Order cancelled as no items were added.");
            return;
        }

        // Corrected Order constructor call
        Order newOrder = new Order(orderID, selectedSupplier, processedBy, orderItems); // Order constructor updated
        orderList.add(newOrder);
        // Assuming Supplier has getSuppliedOrders method which returns a List<Order>
        if (selectedSupplier.getSuppliedOrders() == null) {
            selectedSupplier.setSuppliedOrders(new ArrayList<>()); // Initialize if null
        }
        selectedSupplier.getSuppliedOrders().add(newOrder); // Add order to supplier's list
        System.out.println("\nOrder Placed Successfully!");
        // Assuming displayOrder() method exists in Order class
        // newOrder.displayOrder(); // This is for console output
    }

    // ==== View ====
    public void viewAllOrders() {
        if (orderList.isEmpty()) {
            System.out.println("No orders available.");
            return;
        }
        for (Order o : orderList) {
            // Assuming displayOrder() method exists in Order class
            // o.displayOrder(); // This is for console output
            System.out.println("Order ID: " + o.getOrderID() + ", Supplier: " + o.getSupplier().getName() +
                               ", Processed By: " + o.getProcessedBy() + ", Status: " + o.getStatus() +
                               ", Total: Rs. " + String.format("%.2f", o.calculateTotalAmount()));
            System.out.println("  Items:");
            for (OrderItem item : o.getOrderItems()) { // Corrected to getOrderItems()
                System.out.println("    - " + item.getMedicine().getName() + " x " + item.getQuantity() +
                                   " @ Rs. " + String.format("%.2f", item.getMedicine().getPrice()));
            }
            System.out.println("--------------------"); // Separator for readability
        }
    }

    // ==== Receive ====
    public boolean receiveOrder(String orderID) {
        for (Order order : orderList) {
            if (order.getOrderID().equalsIgnoreCase(orderID)) {
                if (order.getStatus().equalsIgnoreCase("Pending")) {
                    order.setStatus("Received");

                    // Update medicine stock for each item in the order
                    for (OrderItem item : order.getOrderItems()) { // Corrected to getOrderItems()
                        // This updates the *in-memory* stock via MedicineManager
                        if (medicineManager != null) {
                            // Assuming updateMedicineStockGUI(Medicine med, int quantity) adds to stock
                            // and MedicineManager has access to its internal medicineList
                            medicineManager.updateMedicineStockGUI(item.getMedicine(), item.getQuantity());
                            System.out.println("DEBUG: Stock updated for " + item.getMedicine().getName() + " by " + item.getQuantity());
                        } else {
                            System.err.println("ERROR: MedicineManager not initialized. Cannot update stock.");
                        }
                    }
                    return true;
                } else {
                    System.out.println("DEBUG: Order " + orderID + " is not Pending. Current status: " + order.getStatus());
                    return false; // already received or in a different status
                }
            }
        }
        System.out.println("DEBUG: Order " + orderID + " not found.");
        return false; // not found
    }

    // ==== Delete ====
    public boolean deleteOrder(String orderID) {
        Iterator<Order> iterator = orderList.iterator();
        while (iterator.hasNext()) {
            Order order = iterator.next();
            if (order.getOrderID().equalsIgnoreCase(orderID)) {
                if (order.getStatus().equalsIgnoreCase("Pending")) {
                    iterator.remove();
                    // Optional: remove from supplier's suppliedOrders list if desired
                    // Ensure Supplier has getSuppliedOrders() and setSuppliedOrders()
                    if (order.getSupplier() != null && order.getSupplier().getSuppliedOrders() != null) {
                        order.getSupplier().getSuppliedOrders().remove(order);
                    }
                    return true;
                } else {
                    System.out.println("DEBUG: Order " + orderID + " cannot be deleted because its status is " + order.getStatus() + " (only 'Pending' orders can be deleted).");
                    return false; // not deletable if not pending
                }
            }
        }
        System.out.println("DEBUG: Order " + orderID + " not found for deletion.");
        return false;
    }

    // ==== GUI helper methods ====

    // This is the primary method for GUI-based order placement
    // (Note: This still operates on in-memory lists, not directly on the DB)
    public Order placeFinalOrder(String processedBy, Supplier supplier, List<OrderItem> items) { // Renamed parameter
        if (supplier == null || items == null || items.isEmpty()) {
            System.out.println("DEBUG: Cannot place final order: Supplier, items list, or items are empty/null.");
            return null;
        }
        String orderID = generateOrderID(); // Get a new unique ID (in-memory specific)
        
        // Corrected Order constructor call
        Order newOrder = new Order(orderID, supplier, processedBy, new ArrayList<>(items)); // Order constructor updated
        orderList.add(newOrder);
        // Assuming Supplier has getSuppliedOrders method which returns a List<Order>
        if (supplier.getSuppliedOrders() == null) {
            supplier.setSuppliedOrders(new ArrayList<>()); // Initialize if null
        }
        supplier.getSuppliedOrders().add(newOrder);
        System.out.println("DEBUG: Placed order " + newOrder.getOrderID() + " with " + items.size() + " items.");
        return newOrder;
    }

    public String generateOrderID() {
        // Find the highest existing numeric ID and increment it
        int maxId = 0;
        for (Order o : orderList) {
            if (o.getOrderID().startsWith("ORD")) {
                try {
                    int num = Integer.parseInt(o.getOrderID().substring(3));
                    if (num > maxId) {
                        maxId = num;
                    }
                } catch (NumberFormatException e) {
                    // Ignore malformed IDs
                    System.err.println("Warning: Encountered non-numeric Order ID format: " + o.getOrderID());
                }
            }
        }
        return "ORD" + (maxId + 1);
    }

    public Order searchOrder(String orderId) {
        for (Order o : orderList) {
            if (o.getOrderID().equalsIgnoreCase(orderId)) {
                return o;
            }
        }
        return null;
    }

    // Updated updateOrder for in-memory interaction
    // Note: This method's logic for items is for in-memory only and is complex.
    // The GUI's UpdateOrderPanel only updates status and processedBy in DB.
    public boolean updateOrder(String orderId, List<OrderItem> newItems, String newStatus, String processedBy) { // Renamed processedBy
        Order orderToUpdate = searchOrder(orderId);
        if (orderToUpdate == null) {
            return false; // Order not found
        }

        // Update items (replace existing in-memory items)
        // This clears and re-adds the entire list of items for the order in memory.
        orderToUpdate.getOrderItems().clear(); // Corrected to getOrderItems()
        orderToUpdate.getOrderItems().addAll(newItems); // Corrected to getOrderItems()

        orderToUpdate.setStatus(newStatus);
        orderToUpdate.setProcessedBy(processedBy); // Corrected to setProcessedBy()
        return true;
    }


    // ==== Getters ====
    public List<Order> getOrderList() {
        return orderList;
    }

    public List<Supplier> getSupplierList() {
        return supplierList;
    }

    public List<Medicine> getMedicineList() {
        return medicineList;
    }

    // Permissions for GUI (These are still relevant for feature enumeration)
    public static List<String> getAvailablePermissions() {
        return List.of(
                "Place New Order",
                "View All Orders",
                "Approve Order (Receive)",
                "Delete Order",
                "Update Order",
                "Search Order"
        );
    }
}