// const cartService = require('../services/cartService');
// const HttpStatus = require('http-status-codes');

// exports.getCartById = async (req, res) => {
//   try {
//     const { cartId } = req.params;

//     const cartData = await cartService.getCartById(cartId); // Calling service method
//     res.status(HttpStatus.StatusCodes.OK).json(cartData);
    
//   } catch (error) {
//     console.error("Error in cartController:", error.message);

//     const status = error.status || HttpStatus.StatusCodes.INTERNAL_SERVER_ERROR;
//     const message = error.message || "Failed to fetch cart details";

//     res.status(status).json({ error: message });
//   }
// };

// exports.getCartByCustomerId = async (req, res) => {
//   try {
//     const { customerId } = req.params;
//     const cartData = await cartService.getCartByCustomerId(customerId);
//     res.status(HttpStatus.StatusCodes.OK).json(cartData);
//   } catch (error) {
//     console.error("Error in cartController:", error.message);
//     const status = error.status || HttpStatus.StatusCodes.INTERNAL_SERVER_ERROR;
//     const message = error.message || "Failed to fetch cart details";
//     res.status(status).json({ error: message });
//   }
// };

  
// exports.addItemToCart = async (req, res) => {
//   try {
//     const { customerId } = req.params;
//     const cartItemData = req.body;

//     await cartService.addItemToCart(customerId, cartItemData);

//     res.status(HttpStatus.StatusCodes.OK).json({ message: "Item added to cart successfully" });
//   } catch (error) {
//     console.error("Error in cartController (addItemToCart):", error.message);

//     const status = error.status || HttpStatus.StatusCodes.INTERNAL_SERVER_ERROR;
//     const message = error.message || "Failed to add item to cart";

//     res.status(status).json({ error: message });
//   }
// };

// //   // Remove item from cart
// exports.removeItemFromCart = async (req, res) => {
//   try {
//     const { customerId, prodId } = req.params;

//     await cartService.removeItemFromCart(customerId, prodId);

//     res.status(HttpStatus.StatusCodes.OK).json({ message: "Item removed from cart successfully" });
//   } catch (error) {
//     console.error("Error in cartController (removeItemFromCart):", error.message);

//     const status = error.status || HttpStatus.StatusCodes.INTERNAL_SERVER_ERROR;
//     const message = error.message || "Failed to remove item from cart";

//     res.status(status).json({ error: message });
//   }
// };

// //   // Update item quantity
// exports.updateItemQuantity = async (req, res) => {
//   try {
//     const { customerId, prodId } = req.params;
//     const { quantity } = req.body;

//     const updatedItem = await cartService.updateItemQuantity(customerId, prodId, quantity);

//     res.status(HttpStatus.StatusCodes.OK).json(updatedItem);
//   } catch (error) {
//     console.error("Error in cartController (updateItemQuantity):", error.message);

//     const status = error.status || HttpStatus.StatusCodes.INTERNAL_SERVER_ERROR;
//     const message = error.message || "Failed to update item quantity";

//     res.status(status).json({ error: message });
//   }
// };
// //   // Empty cart
// exports.emptyCart = async (req, res) => {
//   try {
//     const { customerId } = req.params;

//     if (!customerId || isNaN(customerId)) {
//       return res.status(400).json({ error: "Invalid customer ID" });
//     }

//     const response = await axios.delete(`${SPRING_BOOT_CART_URL}/customers/${customerId}/empty`);

//     res.status(200).json(response.data);
//   } catch (error) {
//     console.error(`Error emptying cart for customer ID ${req.params.customerId}:`, error.message);

//     if (error.response) {
//       const { status, data } = error.response;
//       return res.status(status).json({ 
//         error: data?.message || "Failed to empty cart" 
//       });
//     }

//     res.status(500).json({ error: "Internal Server Error", details: error.message });
//   }
// };

const cartService = require("../services/cartService");
const { StatusCodes } = require("http-status-codes");
const CustomError = require("../utils/errorUtil");
const HttpStatus = require('http-status-codes').StatusCodes;

exports.getCartById = async (req, res) => {
  try {
    const { cartId } = req.params;
    const cartData = await cartService.getCartById(cartId);
    res.status(StatusCodes.OK).json(cartData);
  } catch (error) {
    res.status(error.status || StatusCodes.INTERNAL_SERVER_ERROR).json({ error: error.message });
  }
};

exports.getCartByCustomerId = async (req, res) => {
  try {
    const { customerId } = req.params;
    const cartData = await cartService.getCartByCustomerId(customerId);
    res.status(StatusCodes.OK).json(cartData);
  } catch (error) {
    res.status(error.status || StatusCodes.INTERNAL_SERVER_ERROR).json({ error: error.message });
  }
};

exports.addItemToCart = async (req, res) => {
  try {
    const { customerId } = req.params;
    const item = req.body;
    await cartService.addItemToCart(customerId, item);
    res.status(StatusCodes.OK).json({ message: "Item added to cart successfully" });
  } catch (error) {
    res.status(error.status || StatusCodes.INTERNAL_SERVER_ERROR).json({ error: error.message });
  }
};

exports.removeItemFromCart = async (req, res) => {
  try {
    const { customerId, prodId } = req.params;
    await cartService.removeItemFromCart(customerId, prodId);
    res.status(StatusCodes.OK).json({ message: "Item removed from cart successfully" });
  } catch (error) {
    res.status(error.status || StatusCodes.INTERNAL_SERVER_ERROR).json({ error: error.message });
  }
};

exports.updateItemQuantity = async (req, res) => {
  try {
    const { customerId, prodId } = req.params;
    const { quantity } = req.body;
    const updatedData = await cartService.updateItemQuantity(customerId, prodId, quantity);
    res.status(StatusCodes.OK).json(updatedData);
  } catch (error) {
    res.status(error.status || StatusCodes.INTERNAL_SERVER_ERROR).json({ error: error.message });
  }
};

exports.emptyCart = async (req, res) => {
  try {
    const { customerId } = req.params;
    if (!customerId || isNaN(customerId)) {
      return res.status(StatusCodes.BAD_REQUEST).json({ error: "Invalid customer ID" });
    }
    const response = await cartService.emptyCart(customerId);
    res.status(StatusCodes.OK).json(response);
  } catch (error) {
    console.error(`Error emptying cart for customer ID ${req.params.customerId}:`, error.message);
    const status = error.status || StatusCodes.INTERNAL_SERVER_ERROR;  // Use the status from CustomError or default to 500
    res.status(status).json({
      error: error.message || "Internal Server Error",
      details: error.details || "Something went wrong while emptying the cart"
    });
  }
};
