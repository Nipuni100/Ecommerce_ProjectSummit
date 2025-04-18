const express = require("express");
const router = express.Router();
const orderController = require("../controllers/orderController");

router.get("/", orderController.getAllOrders);
router.get("/customers/:customerId", orderController.getOrdersByCustomerId);
router.post("/customers/:customerId", orderController.createOrder);
router.patch('/:orderId', orderController.cancelOrder);


module.exports = router;
