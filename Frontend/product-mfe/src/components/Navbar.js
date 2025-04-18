import React from "react";
import { FaSearch, FaShoppingCart } from "react-icons/fa";
import { FiLogOut } from "react-icons/fi";
import { useState } from "react";
import "../pages/styles.css"; // Import your CSS file here


export default function Navbar({ searchTerm, setSearchTerm }) {
  return (
    <nav className="bg-blue-500 p-4 flex justify-between items-center shadow-md">
      <div className="flex items-center gap-4">
       
        <div className="search-bar flex items-center gap-2">
          <input
            type="text"
            placeholder="Search products..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="p-2 rounded-md focus:outline-none"
          />
          <button className="bg-white text-blue-500 p-2 rounded-md">
            🔍
          </button>
        </div>
        <div className="flex items-center gap-6 ml-auto">
        <FaShoppingCart className="text-2xl cursor-pointer text-white" />
        <FiLogOut className="text-2xl cursor-pointer text-white" />

        </div>
      </div>
      
    </nav>
  );
}
