import React from "react";
import { useParams } from "react-router-dom";

export default function CategoryPage({ products }) {
  const { categoryName } = useParams(); // Retrieve category name from the URL

  const filteredProducts = products.filter(
    (product) => product.category.toLowerCase() === categoryName.toLowerCase()
  );

  return (
    <div>
      <h1>{categoryName} Products</h1>
      <div className="grid grid-cols-3 gap-4">
        {filteredProducts.map((product) => (
          <div key={product.id} className="p-4 border rounded">
            <img src={product.image} alt={product.name} />
            <h3>{product.name}</h3>
            <p>{product.price}</p>
          </div>
        ))}
      </div>
    </div>
    

    
  );
}
