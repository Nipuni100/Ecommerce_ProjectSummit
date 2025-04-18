import React from "react";
import ProductCard from "../components/ProductCard";
import { useState } from "react";
import "./styles.css"; 

export default function Dashboard({ products, addToCart }) {
  const [searchTerm, setSearchTerm] = useState("");

  const categories = ["Dairy", "Beverages","Snacks","Fruits","Bakery","Frozen Foods","Canned Goods","Condiments"];
  return (
    <div className="dashboard-container   p-6 font-sans">
      
      {/* Category Navigation */}
      <div className="category-nav">
        {categories.map((category) => (
          <button key={category}>{category}</button>
        ))}
      </div>

      {/* Product Grid */}
      <div className="product-grid">
        {products
          .filter((product) => product.name.toLowerCase().includes(searchTerm.toLowerCase()))
          .map((product) => (
            <div key={product.id} className="product-card">
              <img src={product.image} alt={product.name} />
              <h3>{product.name}</h3>
              <p>{product.description}</p>
              <p className="price">Price: ${product.price}</p>
              <button onClick={() => addToCart(product)}>🛒 Add to Cart</button>
            </div>
          ))}
      </div>
    </div>
  );
}