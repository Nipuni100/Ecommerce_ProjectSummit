import React, { useEffect, useState } from "react";
import axios from "axios";
import ProductCard from "./ProductCard";

export default function ProductCardList() {
  const [products, setProducts] = useState([]);
  const [filteredProducts, setFilteredProducts] = useState([]);
  const [searchQuery, setSearchQuery] = useState("");

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const response = await axios.get(`${process.env.REACT_APP_BFF_URL}/api/products`);
        setProducts(response.data);
        setFilteredProducts(response.data); // Initialize filtered products
      } catch (err) {
        console.error("Error fetching products:", err);
      }
    };

    fetchProducts();
  }, []);

  const handleSearch = (e) => {
    const query = e.target.value.toLowerCase();
    setSearchQuery(query);

    const filtered = products.filter(product =>
      product.name.toLowerCase().includes(query)
    );
    setFilteredProducts(filtered);
  };

  const handleCategoryClick = async (category) => {
    try {
      const response = await axios.get(`${process.env.REACT_APP_BFF_URL}/api/products?category=${category}`);
      setFilteredProducts(response.data);
    } catch (err) {
      console.error("Error fetching category products:", err);
    }
  };

  <div className="flex space-x-4 mb-4">
  {["Cheese", "Milk", "Biscuits"].map(category => (
    <button
      key={category}
      className="bg-gray-200 px-4 py-2 rounded-md hover:bg-gray-300"
      onClick={() => handleCategoryClick(category)}
    >
      {category}
    </button>
  ))}
</div>

  

  return (
    <div>
      <input
        type="text"
        placeholder="Search products..."
        value={searchQuery}
        onChange={handleSearch}
        className="p-2 border rounded-md w-full mb-4"
      />
      <div className="grid grid-cols-3 gap-4">
        {filteredProducts.map(product => (
          <ProductCard key={product.id} product={product} />
        ))}
      </div>
    </div>
  );



}
