const express = require('express');
const productController = require('../controllers/productController');

const router = express.Router();

// Get all products
router.get('/', productController.getProducts);

router.post('/', productController.addProduct);

router.delete('/:prodId', productController.removeProduct);

router.get('/:prodId', productController.getProductById);

router.patch('/:prodId', productController.updateProductStatus);

router.post('/categories', productController.addCategory);
// Update a product by ID
router.get('/categories/:categoryId', productController.getCategoryById);

// Delete a product by ID
router.delete('/categories/:categoryId', productController.deleteCategoryById);


router.put('/categories/:categoryId', productController.updateCategory);


module.exports = router;
