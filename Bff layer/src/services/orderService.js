// src/services/orderService.js
const axios = require('axios');
const { SPRING_BOOT_ORDER_URL, CART_URL } = require('../shared/constants');
const { createError } = require('../utils/errorUtil');
const { StatusCodes } = require('http-status-codes');

const getAllOrders = async (page, size, sort) => {
  try {
    const response = await axios.get(SPRING_BOOT_ORDER_URL, {
      params: { page, size, sort },
    });
    return response.data;
  } catch (error) {
    if (error.response) {
      const { status, data } = error.response;
      throw createError(data?.message || 'Failed to fetch orders', status);
    }
    throw createError('Internal Server Error', 500);
  }
};

const getOrdersByCustomerId = async (customerId) => {
    try {
      const response = await axios.get(`${SPRING_BOOT_ORDER_URL}/customers/${customerId}`);
      return response.data;
    } catch (error) {
      if (error.response) {
        const { status, data } = error.response;
        throw createError(data?.message || 'Failed to fetch customer orders', status || StatusCodes.INTERNAL_SERVER_ERROR);
      }
      throw createError('Internal Server Error', StatusCodes.INTERNAL_SERVER_ERROR);
    }
  };

  const createOrder = async (customerId, orderData) => {
    try {
      const response = await axios.post(`${SPRING_BOOT_ORDER_URL}/customers/${customerId}`, orderData);
      return response.data;
    } catch (error) {
      if (error.response) {
        const { status, data } = error.response;
        throw createError(data?.message || 'Failed to create order', status || StatusCodes.INTERNAL_SERVER_ERROR);
      }
      throw createError('Internal Server Error', StatusCodes.INTERNAL_SERVER_ERROR);
    }
  };
  
  const updateCartAndItemsStatus = async (cartId, status, productDTOs) => {
    try {
      const productIdsInOrder = productDTOs.map((product) => product.prodId);
      if (!productIdsInOrder || productIdsInOrder.length === 0) {
        throw createError("No valid product IDs provided", StatusCodes.BAD_REQUEST);
      }
      await axios.put(`${CART_URL}/${cartId}/status`, {
        status,
        productIdsInOrder, 
      });
    } catch (error) {
      if (error.response) {
        const { status, data } = error.response;
        throw createError(
          data?.message || "Failed to update cart and items status",
          status || StatusCodes.INTERNAL_SERVER_ERROR
        );
      }
      throw createError("Internal Server Error", StatusCodes.INTERNAL_SERVER_ERROR);
    }
  };

  const cancelOrder = async (orderId, status) => {
    try {
    
      const response = await axios.patch(`${SPRING_BOOT_ORDER_URL}/${orderId}`, {
        status: "CANCELLED",
      });
      // Return the response data
      return response.data;
    } catch (error) {
      processServiceError(error, `Failed to cancel order with ID ${orderId}`);
    }
  };
  
  const processServiceError = (error, defaultMessage) => {
    console.error(`Service Error: ${defaultMessage}`, error.message);
  
    if (error.response) {
      const { status, data } = error.response;
      throw createError(data?.message || defaultMessage, status || StatusCodes.INTERNAL_SERVER_ERROR);
    }
  
    throw createError("Internal Server Error", StatusCodes.INTERNAL_SERVER_ERROR);
  };
    
  
module.exports = { cancelOrder,createOrder,updateCartAndItemsStatus,getOrdersByCustomerId,getAllOrders };
