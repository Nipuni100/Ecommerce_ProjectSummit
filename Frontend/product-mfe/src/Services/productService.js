import axios from 'axios';

const BFF_URL = 'http://localhost:5000/api/v1/products';  // Your Node.js BFF Layer URL

export const getProductById = async (prodId) => {
    try {
        const response = await axios.get(`${BFF_URL}/${prodId}`);
        return response.data;  // Return the product data
    } catch (error) {
        console.error('Error fetching product data:', error);
        throw error;
    }
};
