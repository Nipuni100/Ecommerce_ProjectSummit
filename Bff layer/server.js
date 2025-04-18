const express = require('express');
const cors = require('cors');
const productRoutes = require('./src/routes/productRoutes'); 
const cartRoutes = require('./src/routes/cartRoutes');
const orderRoutes = require('./src/routes/orderRoutes');

const app = express();
app.use(express.json()); 
app.use(cors()); 

const corsOptions = {
    origin: "http://localhost:9000", 
    methods: "GET,POST,PUT,DELETE, PATCH",
    credentials: true,   };
  app.use(cors(corsOptions));
  
app.use('/api/v1/products', productRoutes); 
app.use('/api/v1/carts', cartRoutes); 
app.use('/api/v1/orders', orderRoutes);

const PORT = process.env.PORT || 5000;
app.listen(PORT, () => {
    console.log(`BFF running on port ${PORT}`);
});

