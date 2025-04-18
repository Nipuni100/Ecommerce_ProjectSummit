// import React, { useState } from "react";
// import Navbar from "./components/Navbar";
// import Dashboard from "./pages/Dashboard";

// const products = [
//   { id: 1, name: "Product A", price: "$20", image: "https://via.placeholder.com/150" },
//   { id: 2, name: "Product B", price: "$25", image: "https://via.placeholder.com/150" },
//   { id: 3, name: "Product C", price: "$30", image: "https://via.placeholder.com/150" },
// ];

// export default function App() {
//   const [cart, setCart] = useState([]);
//   const [searchTerm, setSearchTerm] = useState("");

//   const addToCart = (product) => {
//     setCart([...cart, product]);
//   };

//   return (
//     <div className="min-h-screen bg-gray-100">
//       <Navbar searchTerm={searchTerm} setSearchTerm={setSearchTerm} />
//       <Dashboard products={products} addToCart={addToCart} searchTerm={searchTerm} />
//     </div>
//   );
// }

import React, { useState } from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Navbar from "./components/Navbar";
import Dashboard from "./pages/Dashboard";
import CategoryPage from "./pages/CategoryPage";
import ProductCard from "./components/ProductCard";
import ProductDetails from "./components/ProductDetails"; // A new page for categories

const products = [
  {  prodId: 1, name: "Milk", price: "$20", image: "https://via.placeholder.com/150", category: "Cheese" },
  {  prodId: 2, name: "Cheese", price: "$25", image: "https://via.placeholder.com/150", category: "Milk" },
  {  prodId: 3, name: "Shrimps", price: "$30", image: "https://via.placeholder.com/150", category: "Biscuits" },
];

export default function App() {
  const [cart, setCart] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");

  const addToCart = (product) => {
    setCart([...cart, product]);
  };

  return (
    <Router>
      <div className="min-h-screen bg-gray-100">
        <Navbar searchTerm={searchTerm} setSearchTerm={setSearchTerm} />
        <Routes>
          <Route path="/" element={<Dashboard products={products} addToCart={addToCart} searchTerm={searchTerm} />} />
          <Route path="/product/:prodId" element={<ProductDetails />} />
          <Route path="/category/:categoryName" element={<CategoryPage products={products} />} />
        </Routes>
      </div>
    </Router>
  ); }
