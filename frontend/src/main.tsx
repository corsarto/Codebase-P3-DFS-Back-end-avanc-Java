import { FormEvent, StrictMode, useEffect, useState } from 'react';
import { createRoot } from 'react-dom/client';
import './styles.css';

type Equipment = { id: number; name: string; category: string; available: boolean; borrowerFirstName?: string };

function App() {
  const [equipment, setEquipment] = useState<Equipment[]>([]);
  const [selectedId, setSelectedId] = useState<number | null>(null);
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');

  const loadEquipment = () => fetch('/api/equipment').then(response => response.json()).then(setEquipment);
  useEffect(() => { void loadEquipment(); }, []);

  const borrow = async (event: FormEvent) => {
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

  return <main><header><p className="eyebrow">Internal equipment desk</p><h1>EquipIT</h1></header>
    <section className="equipment-list">{equipment.map(item => <article key={item.id}>
      <div><h2>{item.name}</h2><p>{item.category}</p></div><div className="actions">
        <span className={item.available ? 'available' : 'borrowed'}>{item.available ? 'Available' : `Borrowed by ${item.borrowerFirstName ?? 'a colleague'}`}</span>
        {item.available ? <button onClick={() => setSelectedId(item.id)}>Borrow</button> : <button onClick={() => void returnEquipment(item.id)}>Return</button>}
      </div></article>)}</section>
    {selectedId !== null && <form onSubmit={borrow}><h2>Borrow equipment</h2><input required placeholder="First name" value={firstName} onChange={event => setFirstName(event.target.value)} /><input required placeholder="Last name" value={lastName} onChange={event => setLastName(event.target.value)} /><button type="submit">Confirm</button></form>}
  </main>;
}

createRoot(document.getElementById('root')!).render(<StrictMode><App /></StrictMode>);