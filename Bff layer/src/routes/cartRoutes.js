const express = require('express');
const cartController = require('../controllers/cartController');
const router = express.Router();

// Get cart by cart ID
router.get('/:cartId', cartController.getCartById);
// Get cart by customer ID
router.get('/customers/:customerId', cartController.getCartByCustomerId);
// // Add item to cart
router.post('/customers/:customerId/items', cartController.addItemToCart);
// // Remove item from cart
router.delete('/customers/:customerId/items/:prodId', cartController.removeItemFromCart);
// // Update item quantity
router.patch('/customers/:customerId/items/:prodId', cartController.updateItemQuantity);
// // Empty the cart
router.delete('/customers/:customerId/empty', cartController.emptyCart);
// // Get all carts
// router.get('/', cartController.getAllCarts);

module.exports = router;