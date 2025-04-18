const axios = require('axios');
const ApiError = require('../utils/ApiError');
const { StatusCodes } = require('http-status-codes');
const { SPRING_BOOT_PRODUCTS_URL } = require('../shared/constants');



// const SPRING_BOOT_BASE_URL = process.env.SPRING_BOOT_BASE_URL || 'http://localhost:8080/products';

exports.handleProductSearch = async (query) => {
    validateSearchQuery(query.searchQuery);

    const url = buildProductSearchUrl(query);
    const response = await axios.get(url);

    return response.data;
};

function validateSearchQuery(searchQuery) {
    if (!searchQuery || searchQuery.trim() === "") {
        throw new ApiError(StatusCodes.BAD_REQUEST, "Search query cannot be empty.");
    }
}

function buildProductSearchUrl({ searchQuery, page = 0, size = 10, sort }) {
    let url = `${SPRING_BOOT_PRODUCTS_URL}?searchQuery=${encodeURIComponent(searchQuery)}&page=${page}&size=${size}`;
    if (sort) {
        url += `&sort=${encodeURIComponent(sort)}`;
    }
    return url;
}

exports.fetchProductById = async (productId) => {
    try {
        const response = await axios.get(`${SPRING_BOOT_PRODUCTS_URL}/${productId}`);
        return response.data;
    } catch (error) {
        console.error('Error fetching product from Spring Boot backend:', error.message);
        throw new ApiError(
            error.response?.status || StatusCodes.INTERNAL_SERVER_ERROR,
            'Failed to fetch product from backend'
        );
    }
};

exports.deleteProductById = async (productId) => {
    try {
        await axios.delete(`${SPRING_BOOT_PRODUCTS_URL}/${productId}`);
    } catch (error) {
        console.error('Error deleting product from backend:', error.message);
        throw new ApiError(
            error.response?.status || StatusCodes.INTERNAL_SERVER_ERROR,
            `Failed to delete product with ID ${productId}`
        );
    }
};

exports.createProduct = async (productData) => {
    try {
        const response = await axios.post(SPRING_BOOT_PRODUCTS_URL, productData);
        return response.data;
    } catch (error) {
        console.error('Error adding product to backend:', error.message);
        throw new ApiError(
            error.response?.status || StatusCodes.INTERNAL_SERVER_ERROR,
            'Failed to add product to backend'
        );
    }
};

exports.updateProductStatus = async (prodId, status) => {
    try {
        const statusDTO = { status: status.trim() };
        const response = await axios.patch(`${SPRING_BOOT_PRODUCTS_URL}/${prodId}`, statusDTO);
        return response.data;
    } catch (error) {
        console.error('Error updating product status in backend:', error.message);
        throw new ApiError(
            error.response?.status || StatusCodes.INTERNAL_SERVER_ERROR,
            error.response?.data?.message || 'Failed to update product status in backend'
        );
    }
};

exports.getAllCategories = async ({ page = 0, size = 10, sort }) => {
    try {
        const queryParams = new URLSearchParams({ page, size });
        if (sort) queryParams.append('sort', sort);

        const url = `${SPRING_BOOT_PRODUCTS_URL}/categories?${queryParams.toString()}`;
        console.log(`Fetching categories from: ${url}`);

        const response = await axios.get(url);
        return response.data;
    } catch (error) {
        console.error("Error fetching paginated categories:", error.message);
        throw new ApiError(
            error.response?.status || StatusCodes.INTERNAL_SERVER_ERROR,
            error.response?.data?.message || "Failed to fetch categories from backend"
        );
    }
};

exports.getCategoryById = async (categoryId) => {
    if (isNaN(categoryId) || categoryId <= 0) {
        throw new ApiError(StatusCodes.BAD_REQUEST, "Invalid category ID");
    }

    try {
        const response = await axios.get(`${SPRING_BOOT_PRODUCTS_URL}/categories/${categoryId}`);
        return response.data;
    } catch (error) {
        console.error('Error fetching category:', error.message);

        const status = error.response?.status || StatusCodes.INTERNAL_SERVER_ERROR;
        const message = error.response?.data?.message || 'Failed to fetch category from backend';

        throw new ApiError(status, message);
    }
};

exports.deleteCategoryById = async (categoryId) => {
    if (isNaN(categoryId) || categoryId <= 0) {
        throw new ApiError(StatusCodes.BAD_REQUEST, "Invalid category ID");
    }

    try {
        const response = await axios.delete(`${SPRING_BOOT_PRODUCTS_URL}/categories/${categoryId}`);
        return response;
    } catch (error) {
        console.error("Service error deleting category:", error.message);

        const status = error.response?.status || StatusCodes.INTERNAL_SERVER_ERROR;
        const message = error.response?.data?.message || "Failed to delete category from backend";

        throw new ApiError(status, message);
    }
};

exports.addCategory = async ({ categoryName, categoryDescription }) => {
    if (
        !categoryName || typeof categoryName !== 'string' || categoryName.trim() === '' ||
        !categoryDescription || typeof categoryDescription !== 'string' || categoryDescription.trim() === ''
    ) {
        throw new ApiError(StatusCodes.BAD_REQUEST, 'Both categoryName and categoryDescription are required');
    }

    const payload = {
        categoryName: categoryName.trim(),
        categoryDescription: categoryDescription.trim(),
    };

    try {
        const response = await axios.post(`${SPRING_BOOT_PRODUCTS_URL}/categories/`, payload);
        return response.data;
    } catch (error) {
        console.error('Service error adding category:', error.message);
        const status = error.response?.status || StatusCodes.INTERNAL_SERVER_ERROR;
        const message = error.response?.data?.message || 'Failed to add category to backend';
        throw new ApiError(status, message);
    }
};

exports.updateCategory = async (categoryId, categoryData) => {
    if (!categoryId || isNaN(categoryId) || categoryId <= 0) {
        throw new ApiError(StatusCodes.BAD_REQUEST, "Invalid category ID");
    }

    const { categoryName, categoryDescription } = categoryData;

    if (!categoryName || typeof categoryName !== "string" || categoryName.trim() === "") {
        throw new ApiError(StatusCodes.BAD_REQUEST, "Invalid category name");
    }

    const payload = {
        categoryName: categoryName.trim(),
        categoryDescription: categoryDescription ? categoryDescription.trim() : ""
    };

    try {
        const response = await axios.put(`${SPRING_BOOT_PRODUCTS_URL}/categories/${categoryId}`, payload);
        return response.data;
    } catch (error) {
        console.error("Service error updating category:", error.response?.data || error.message);
        const status = error.response?.status || StatusCodes.INTERNAL_SERVER_ERROR;
        const message = error.response?.data?.message || "Failed to update category in backend";
        throw new ApiError(status, message);
    }
};
