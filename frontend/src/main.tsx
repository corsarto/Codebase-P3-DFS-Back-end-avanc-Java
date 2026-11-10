import {SubmitEvent, StrictMode, useEffect, useState } from 'react';
import { createRoot } from 'react-dom/client';
import { translations } from './languageChange';
import './styles.css';

type Equipment = { 
  id: number; 
  name: string; 
  category: string; 
  available: boolean; 
  borrowerFirstName?: string;
  borrowerLastName?: string;
  borrowedAt?: string;
  lastReturnedAt?: string 
};

function App() {
  const [equipment, setEquipment] = useState<Equipment[]>([]);
  const [selectedId, setSelectedId] = useState<number | null>(null);
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [language, setLanguage] = useState<"en" | "fr">("en");

  const loadEquipment = () => fetch('/api/equipment').then(response => response.json()).then(setEquipment);
  useEffect(() => { void loadEquipment(); }, []);

  const borrow = async (event: SubmitEvent) => {
    event.preventDefault();
    if (selectedId === null) return;
    await fetch(`/api/equipment/${selectedId}/borrow`, {
      method: 'PUT', headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ firstName, lastName })
    });
    setSelectedId(null); setFirstName(''); setLastName(''); await loadEquipment();
  };

  const returnEquipment = async (id: number) => {
    await fetch(`/api/equipment/${id}/return`, { method: 'PUT' });
    await loadEquipment();
  };

  const text = translations[language];

  return <main>
          <header>
            <p className="eyebrow">{text.title}</p>
            <h1>EquipIT</h1>
            <button onClick={() => setLanguage('fr')}>FR</button>
            <button onClick={() => setLanguage('en')}>EN</button>
          </header>
          <table>
            <thead>
              <tr>
                <th>{text.name}</th>
                <th>{text.category}</th>
                <th>{text.status}</th>
                <th>{text.borrower}</th>
                <th>{text.borrowedAt}</th>
                <th>{text.lastReturnedAt}</th>
                <th>{text.action}</th>
              </tr>
            </thead>
            <tbody>
              {equipment.map(item =>
                <tr key={item.id}> 
                  <td>{item.name}</td>
                  <td>{item.category}</td>
                  <td className={item.available ? 'available' : 'borrowed'}>
                    {item.available ? text.available : text.borrowed}
                  </td>
                  <td>{item.available ? '' : `${item.borrowerFirstName} ${item.borrowerLastName}`}</td>
                  <td>{item.borrowedAt}</td>
                  <td>{item.lastReturnedAt}</td>
                  <td>
                    {item.available ? 
                      <button onClick={() => setSelectedId(item.id)}>{text.buttonBorrow}</button> 
                      : 
                      <button onClick={() => void returnEquipment(item.id)}>{text.buttonReturn}</button>}
                  </td>
                </tr>
              )}
            </tbody>
          </table>
          {selectedId !== null && 
            <form onSubmit={borrow}>
              <h2>{text.formTitle}</h2>
              <input 
                required placeholder={text.placeholderFirstName}
                value={firstName} 
                onChange={event => setFirstName(event.target.value)} 
              />
              <input 
                required placeholder={text.placeholderLastName}
                value={lastName} 
                onChange={event => setLastName(event.target.value)} 
              />
              <button type="submit">{text.buttonConfirm}</button>
            </form>}
  </main>;
}

createRoot(document.getElementById('root')!).render(<StrictMode><App /></StrictMode>);