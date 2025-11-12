import React, { useState } from 'react';
import axios from 'axios';

function AddDealer({ onDealerAdded }) {
  const [dealer, setDealer] = useState({
    dealerCode: '',
    dealerName: '',
    category: '',
    creditLimit: '',
    balance: 0
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setDealer({ ...dealer, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post('http://localhost:8080/dealers', dealer);
      alert('Dealer successfully added!');
      onDealerAdded(); // listeyi güncellemek için parent fonksiyonu çağır
      setDealer({ dealerCode: '', dealerName: '', category: '', creditLimit: '', balance: 0 });
    } catch (error) {
      console.error('Error adding dealer:', error);
      alert('Failed to add dealer');
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ marginBottom: '20px' }}>
      <h3>Add New Dealer</h3>
      <input
        type="text"
        name="dealerCode"
        placeholder="Dealer Code"
        value={dealer.dealerCode}
        onChange={handleChange}
        required
      />
      <input
        type="text"
        name="dealerName"
        placeholder="Dealer Name"
        value={dealer.dealerName}
        onChange={handleChange}
        required
      />
      <input
        type="text"
        name="category"
        placeholder="Category (GOLD/SILVER)"
        value={dealer.category}
        onChange={handleChange}
        required
      />
      <input
        type="number"
        name="creditLimit"
        placeholder="Credit Limit"
        value={dealer.creditLimit}
        onChange={handleChange}
        required
      />
      <button type="submit">Add Dealer</button>
    </form>
  );
}

export default AddDealer;
