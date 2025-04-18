import React from "react";
import axios from "axios";

export default function ProductCard({ product }) {
  const addToCart = async () => {
    try {
      await axios.post(`${process.env.REACT_APP_BFF_URL}/api/cart`, {
        productId: product.prodId,
        name: product.prodName,
        price: product.price,
        image: product.image
      });
      alert("Added to cart!");
    } catch (err) {
      console.error("Error adding to cart:", err);
    }
  };

  return (
    <div className="bg-white p-4 rounded-xl shadow-lg">
      <img src={product.image} alt={product.name} className="w-full h-40 object-cover rounded-md" />
      <h3 className="text-lg font-bold mt-2">{product.name}</h3>
      <p className="text-gray-600">{product.price}</p>
      <button
        className="mt-4 bg-blue-500 text-white px-4 py-2 rounded-md w-full hover:bg-blue-600"
        onClick={addToCart}
      >
        Add to Cart
      </button>
    </div>
  );
}
