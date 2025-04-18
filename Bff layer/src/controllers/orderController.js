
const { createOrder, updateCartAndItemsStatus ,getOrdersByCustomerId,getAllOrders } = require('../services/orderService');
const { StatusCodes } = require('http-status-codes');
const orderService = require('../services/orderService'); // Ensure this is correctly imported


exports.getAllOrders = async (req, res) => {
  try {
    const { page = 0, size = 10, sort } = req.query;
    const orders = await getAllOrders(page, size, sort);
    res.status(StatusCodes.OK).json(orders); // Use StatusCodes.OK for 200
  } catch (error) {
    console.error('Error fetching all orders:', error.message || error);
    const status = error.status || StatusCodes.INTERNAL_SERVER_ERROR;
    res.status(status).json({ error: error.message });
  }
};


exports.getOrdersByCustomerId = async (req, res) => {
  try {
    const { customerId } = req.params;

    if (!customerId || isNaN(customerId)) {
      return res
        .status(StatusCodes.BAD_REQUEST) // Use StatusCodes.BAD_REQUEST for 400
        .json({ error: 'Invalid customer ID' });
    }

    const orders = await getOrdersByCustomerId(customerId);
    res.status(StatusCodes.OK).json(orders); // Use StatusCodes.OK for 200
  } catch (error) {
    console.error(`Error fetching orders for customer ID ${req.params.customerId}:`, error.message || error);
    const status = error.status || StatusCodes.INTERNAL_SERVER_ERROR;
    res.status(status).json({ error: error.message });
  }
};

exports.createOrder = async (req, res) => {
  try {
    const { customerId } = req.params;
    const { cartId, paymentMethod, orderStatus, productList } = req.body; // `productList` is a list of productDTOs

    // Input validation
    if (!customerId || isNaN(customerId)) {
      return res
        .status(StatusCodes.BAD_REQUEST)
        .json({ error: "Invalid customer ID" });
    }

    if (
      !cartId ||
      !paymentMethod ||
      !orderStatus ||
      !Array.isArray(productList) ||
      productList.length === 0
    ) {
      return res
        .status(StatusCodes.BAD_REQUEST)
        .json({ error: "Missing or invalid order data" });
    }

    // Extract product IDs from productDTOs
    const productIdsInOrder = productList.map((product) => product.prodId);
    if (!productIdsInOrder || productIdsInOrder.length === 0) {
      return res
        .status(StatusCodes.BAD_REQUEST)
        .json({ error: "Product list contains no valid product IDs" });
    }

    // Create the order
    const orderData = {
      cartId,
      paymentMethod,
      orderStatus,
      productList, // Pass the full productDTOs to the backend
    };

    const orderResponse = await createOrder(customerId, orderData);

    // Update cart and items statuses in one call
    await updateCartAndItemsStatus(cartId, "DEACTIVE", productList);

    res.status(StatusCodes.CREATED).json(orderResponse);
  } catch (error) {
    console.error(
      `Error creating order for customer ID ${req.params.customerId}:`,
      error.message || error
    );
    const status = error.status || StatusCodes.INTERNAL_SERVER_ERROR;
    res.status(status).json({ error: error.message || "Failed to create order" });
  }
};

  
  exports.cancelOrder = async (req, res) => {
    try {
      const { orderId } = req.params;
      const { status } = req.body;
  
      // Input validation
      if (!orderId || isNaN(orderId)) {
        throw createError("Invalid order ID", StatusCodes.BAD_REQUEST);
      }
      if (!status) {
        throw createError("Missing order status", StatusCodes.BAD_REQUEST);
      }
      const result = await orderService.cancelOrder(orderId, status);
      res.status(StatusCodes.OK).json({ message: result });
    } catch (error) {
      handleError(res, error);
    }
  };
  
  // Centralized error handling function for controllers
  const handleError = (res, error) => {
    console.error(`Error: ${error.message}`);
    const status = error.status || StatusCodes.INTERNAL_SERVER_ERROR;
    res.status(status).json({ error: error.message || "Internal Server Error" });
  };
