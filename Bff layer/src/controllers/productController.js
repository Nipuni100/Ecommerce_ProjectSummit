const productService = require('../services/productService');
const ApiError = require('../utils/ApiError');
const { StatusCodes, getReasonPhrase } = require('http-status-codes');

exports.getProducts = async (req, res) => {
    try {
        const products = await productService.handleProductSearch(req.query);
        res.status(StatusCodes.OK).json(products);
    } catch (error) {
        console.error('Error fetching products:', error);

        const status = error instanceof ApiError
            ? error.statusCode
            : error.response?.status || StatusCodes.INTERNAL_SERVER_ERROR;

        const message = error instanceof ApiError
            ? error.message
            : error.response?.data?.message || getReasonPhrase(StatusCodes.INTERNAL_SERVER_ERROR);

        res.status(status).json({ error: message });
    }
};


// Get product by ID
exports.getProductById = async (req, res) => {
    try {
        const product = await productService.fetchProductById(req.params.prodId);
        res.status(StatusCodes.OK).json(product);
    } catch (error) {
        console.error('Controller error:', error.message);
        res.status(error.statusCode || StatusCodes.INTERNAL_SERVER_ERROR).json({ error: error.message });
    }
};

// Remove a product
exports.removeProduct = async (req, res) => {
    try {
        await productService.deleteProductById(req.params.prodId);
        res.status(StatusCodes.OK).json({ message: `Product with ID ${req.params.prodId} deleted successfully.` });
    } catch (error) {
        console.error('Controller error:', error.message);
        res.status(error.statusCode || StatusCodes.INTERNAL_SERVER_ERROR).json({ error: error.message });
    }
};


// Add a new product
exports.addProduct = async (req, res) => {
    try {
        const createdProduct = await productService.createProduct(req.body);
        res.status(StatusCodes.CREATED).json(createdProduct);
    } catch (error) {
        console.error('Controller error:', error.message);
        res.status(error.statusCode || StatusCodes.INTERNAL_SERVER_ERROR).json({ error: error.message });
    }
};



exports.updateProductStatus = async (req, res) => {
    try {
        const { status } = req.body;
        const { prodId } = req.params;

        if (!status || typeof status !== "string" || status.trim() === "") {
            return res.status(StatusCodes.BAD_REQUEST).json({ error: "Invalid status value" });
        }

        const updatedProduct = await productService.updateProductStatus(prodId, status);
        res.status(StatusCodes.OK).json(updatedProduct);
    } catch (error) {
        console.error('Controller error:', error.message);
        res.status(error.statusCode || StatusCodes.INTERNAL_SERVER_ERROR).json({ error: error.message });
    }
};



// Get all categories
exports.getAllCategories = async (req, res) => {
    try {
        const categories = await productService.getAllCategories(req.query);
        res.status(StatusCodes.OK).json(categories);
    } catch (error) {
        console.error("Controller error:", error.message);
        res.status(error.statusCode || StatusCodes.INTERNAL_SERVER_ERROR).json({ error: error.message });
    }
};

exports.getCategoryById = async (req, res) => {
    try {
        const { categoryId } = req.params;
        const category = await productService.getCategoryById(categoryId);
        res.status(StatusCodes.OK).json(category);
    } catch (error) {
        console.error("Controller error:", error.message);
        res.status(error.statusCode || StatusCodes.INTERNAL_SERVER_ERROR).json({ error: error.message });
    }
};


exports.deleteCategoryById = async (req, res) => {
    try {
        const { categoryId } = req.params;
        const response = await productService.deleteCategoryById(categoryId);

        if (response.status === StatusCodes.NO_CONTENT) {
            return res.status(StatusCodes.NO_CONTENT).json({ message: "Category deleted successfully" });
        } else {
            return res.status(response.status).json({ message: response.data });
        }

    } catch (error) {
        console.error("Controller error deleting category:", error.message);
        res.status(error.statusCode || StatusCodes.INTERNAL_SERVER_ERROR).json({ error: error.message });
    }
};

// Add a category
exports.addCategory = async (req, res) => {
    try {
        const categoryData = req.body;
        const createdCategory = await productService.addCategory(categoryData);
        res.status(StatusCodes.CREATED).json(createdCategory);
    } catch (error) {
        console.error('Controller error adding category:', error.message);
        res.status(error.statusCode || StatusCodes.INTERNAL_SERVER_ERROR).json({ error: error.message });
    }
};

// Update a category
exports.updateCategory = async (req, res) => {
    try {
        const { categoryId } = req.params;
        const categoryData = req.body;

        const updatedCategory = await productService.updateCategory(categoryId, categoryData);
        res.status(StatusCodes.OK).json(updatedCategory);
    } catch (error) {
        console.error("Controller error updating category:", error.message);
        res.status(error.statusCode || StatusCodes.INTERNAL_SERVER_ERROR).json({ error: error.message });
    }
};







