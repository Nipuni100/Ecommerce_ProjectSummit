// const axios = require('axios');
// const { CART_URL } = require('../constants/urls');
// const HttpStatus = require('http-status-codes');

// exports.getCartById = async (cartId) => {
//   if (isNaN(cartId)) {
//     const err = new Error("Invalid cart ID");
//     err.status = HttpStatus.StatusCodes.BAD_REQUEST;
//     throw err;
//   }

//   try {
//     const response = await axios.get(`${CART_URL}/${cartId}`);
//     return response.data;

//   } catch (error) {
//     console.error("Error in cartService:", error.message);

//     if (error.response) {
//       const status = error.response.status;
//       const message = error.response.data?.message || "Failed to fetch cart details";
//       const err = new Error(message);
//       err.status = status;
//       throw err;
//     }

//     const err = new Error("Failed to fetch cart details");
//     err.status = HttpStatus.StatusCodes.INTERNAL_SERVER_ERROR;
//     throw err;
//   }
// };

// exports.getCartByCustomerId = async (customerId) => {
//     if (!customerId || isNaN(customerId)) {
//       const error = new Error("Invalid customer ID");
//       error.status = HttpStatus.StatusCodes.BAD_REQUEST;
//       throw error;
//     }
  
//     try {
//       const response = await axios.get(`${CART_URL}/customers/${customerId}`);
  
//       if (!response.data) {
//         const error = new Error("Cart not found for this customer");
//         error.status = HttpStatus.StatusCodes.NOT_FOUND;
//         throw error;
//       }
  
//       return response.data;
//     } catch (error) {
//       console.error("Error in cartService:", error.message);
  
//       if (error.response) {
//         const status = error.response.status;
//         const message = error.response.data?.message || "Failed to fetch cart details";
//         const err = new Error(message);
//         err.status = status;
//         throw err;
//       }
  
//       const err = new Error("Failed to fetch cart details");
//       err.status = HttpStatus.StatusCodes.INTERNAL_SERVER_ERROR;
//       throw err;
//     }
//   };

//   exports.addItemToCart = async (customerId, cartItemData) => {
//     if (!customerId || isNaN(customerId)) {
//       const err = new Error("Invalid customer ID");
//       err.status = HttpStatus.StatusCodes.BAD_REQUEST;
//       throw err;
//     }
  
//     const { prodId, quantity, price } = cartItemData;
  
//     if (!prodId || !quantity || !price) {
//       const err = new Error("Missing required fields: prodId, quantity, and price");
//       err.status = HttpStatus.StatusCodes.BAD_REQUEST;
//       throw err;
//     }
  
//     try {
//       await axios.post(`${CART_URL}/customers/${customerId}/items`, cartItemData);
//     } catch (error) {
//       console.error("Error in cartService (addItemToCart):", error.message);
  
//       if (error.response) {
//         const status = error.response.status;
//         const message = error.response.data?.message || "Failed to add item to cart";
//         const err = new Error(message);
//         err.status = status;
//         throw err;
//       }
  
//       const err = new Error("Failed to add item to cart");
//       err.status = HttpStatus.StatusCodes.INTERNAL_SERVER_ERROR;
//       throw err;
//     }
//   };

//   exports.removeItemFromCart = async (customerId, prodId) => {
//     if (!customerId || isNaN(customerId)) {
//       const err = new Error("Invalid customer ID");
//       err.status = HttpStatus.StatusCodes.BAD_REQUEST;
//       throw err;
//     }
  
//     if (!prodId || isNaN(prodId)) {
//       const err = new Error("Invalid item ID");
//       err.status = HttpStatus.StatusCodes.BAD_REQUEST;
//       throw err;
//     }
  
//     try {
//       await axios.delete(`${CART_URL}/customers/${customerId}/items/${prodId}`);
//     } catch (error) {
//       console.error("Error in cartService (removeItemFromCart):", error.message);
  
//       if (error.response) {
//         const { status, data } = error.response;
  
//         const message =
//           (status === HttpStatus.StatusCodes.NOT_FOUND && data?.message) ||
//           data?.message ||
//           "Failed to remove item from cart";
  
//         const err = new Error(message);
//         err.status = status;
//         throw err;
//       }
  
//       const err = new Error("Failed to remove item from cart");
//       err.status = HttpStatus.StatusCodes.INTERNAL_SERVER_ERROR;
//       throw err;
//     }
//   };

//   exports.updateItemQuantity = async (customerId, prodId, quantity) => {
//     if (!customerId || isNaN(customerId)) {
//       const err = new Error("Invalid customer ID");
//       err.status = HttpStatus.StatusCodes.BAD_REQUEST;
//       throw err;
//     }
  
//     if (!prodId || isNaN(prodId)) {
//       const err = new Error("Invalid item ID");
//       err.status = HttpStatus.StatusCodes.BAD_REQUEST;
//       throw err;
//     }
  
//     if (quantity === undefined || quantity <= 0) {
//       const err = new Error("Invalid quantity. Quantity must be greater than 0.");
//       err.status = HttpStatus.StatusCodes.BAD_REQUEST;
//       throw err;
//     }
  
//     try {
//       const response = await axios.patch(
//         `${CART_URL}/customers/${customerId}/items/${prodId}`,
//         { quantity }
//       );
  
//       return response.data;
//     } catch (error) {
//       console.error("Error in cartService (updateItemQuantity):", error.message);
  
//       if (error.response) {
//         const { status, data } = error.response;
  
//         const message =
//           (status === HttpStatus.StatusCodes.NOT_FOUND && data?.message) ||
//           data?.message ||
//           "Failed to update item quantity";
  
//         const err = new Error(message);
//         err.status = status;
//         throw err;
//       }
  
//       const err = new Error("Failed to update item quantity");
//       err.status = HttpStatus.StatusCodes.INTERNAL_SERVER_ERROR;
//       throw err;
//     }
//   };

const axios = require("axios");
const { StatusCodes } = require("http-status-codes");
const { CART_URL } = require('../shared/constants');
const { createError } = require("../utils/errorUtil");
const CustomError = require("../utils/errorUtil");

exports.getCartById = async (cartId) => {
  if (isNaN(cartId)) throw createError("Invalid cart ID", StatusCodes.BAD_REQUEST);
  try {
    const response = await axios.get(`${CART_URL}/${cartId}`);
    return response.data;
  } catch (error) {
    const { status, data } = error.response || {};
    throw createError(data?.message || "Failed to fetch cart details", status || StatusCodes.INTERNAL_SERVER_ERROR);
  }
};

exports.getCartByCustomerId = async (customerId) => {
  if (!customerId || isNaN(customerId)) throw createError("Invalid customer ID", StatusCodes.BAD_REQUEST);
  try {
    const response = await axios.get(`${CART_URL}/customers/${customerId}`);
    if (!response.data) throw createError("Cart not found for this customer", StatusCodes.NOT_FOUND);
    return response.data;
  } catch (error) {
    const { status, data } = error.response || {};
    throw createError(data?.message || "Failed to fetch cart details", status || StatusCodes.INTERNAL_SERVER_ERROR);
  }
};

exports.addItemToCart = async (customerId, item) => {
  if (!customerId || isNaN(customerId)) throw createError("Invalid customer ID", StatusCodes.BAD_REQUEST);
  if (!item.prodId || !item.quantity || !item.price) {
    throw createError("Missing required fields: prodId, quantity, and price", StatusCodes.BAD_REQUEST);
  }

  try {
    await axios.post(`${CART_URL}/customers/${customerId}/items`, item);
  } catch (error) {
    const { status, data } = error.response || {};
    throw createError(data?.message || "Failed to add item to cart", status || StatusCodes.INTERNAL_SERVER_ERROR);
  }
};

exports.removeItemFromCart = async (customerId, prodId) => {
  if (!customerId || isNaN(customerId)) throw createError("Invalid customer ID", StatusCodes.BAD_REQUEST);
  if (!prodId || isNaN(prodId)) throw createError("Invalid item ID", StatusCodes.BAD_REQUEST);

  try {
    await axios.delete(`${CART_URL}/customers/${customerId}/items/${prodId}`);
  } catch (error) {
    const { status, data } = error.response || {};
    throw createError(data?.message || "Failed to remove item from cart", status || StatusCodes.INTERNAL_SERVER_ERROR);
  }
};

exports.updateItemQuantity = async (customerId, prodId, quantity) => {
  if (!customerId || isNaN(customerId)) throw createError("Invalid customer ID", StatusCodes.BAD_REQUEST);
  if (!prodId || isNaN(prodId)) throw createError("Invalid item ID", StatusCodes.BAD_REQUEST);
  if (quantity === undefined || quantity <= 0) throw createError("Invalid quantity. Quantity must be greater than 0.", StatusCodes.BAD_REQUEST);

  try {
    const response = await axios.patch(`${CART_URL}/customers/${customerId}/items/${prodId}`, { quantity });
    return response.data;
  } catch (error) {
    const { status, data } = error.response || {};
    throw createError(data?.message || "Failed to update item quantity", status || StatusCodes.INTERNAL_SERVER_ERROR);
  }
};

exports.emptyCart = async (customerId) => {
    if (isNaN(customerId)) {
      throw new CustomError("Invalid customer ID", StatusCodes.BAD_REQUEST);  // Throwing CustomError with BAD_REQUEST status
    }
    try {
      const response = await axios.delete(`${CART_URL}/customers/${customerId}/empty`);
      return response.data;
    } catch (error) {
      if (error.response) {
        const message = error.response.data?.message || "Failed to empty cart";
        throw new CustomError(message, error.response.status);
      }
      throw new CustomError("Failed to empty cart", StatusCodes.INTERNAL_SERVER_ERROR);  // Default to 500
}
  };
