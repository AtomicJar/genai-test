import './App.css';
import React from 'react';

function App() {

    const [loading, setLoading] = React.useState(false);
    const [messages, setMessages] = React.useState([]);
    const [ragMessages, setRagMessages] = React.useState([]);

    const [input, setInput] = React.useState('');
    const chatWindow = React.useRef();

    // After render, this scrolls the textArea to the bottom.
    React.useEffect(() => {
        const area = chatWindow.current;
        if (area) {
            area.scrollTop = area.scrollHeight;
        }
    });

    const sendMessage = (e) => {
        e.preventDefault();
        if (input.trim()) {
            setInput(() => '');
            setLoading(true);

            sendMessageToPath('/chat/straight', setMessages);
            sendMessageToPath('/chat/rag', setRagMessages);
        }
    };

    const sendMessageToPath = (path, setter)=> {
        const temporalMessage = [...messages, {text: input.trim(), sender: 'user'}];
        fetch(`${path}?question=${input}`)
            .then((response) => response.json())
            .then((chatResponse) => {
                setter([...temporalMessage, {text: chatResponse.message, sender: 'ai'}]);
            })
            .catch(error => console.log(error))
            .finally(() => setLoading(false));
    }

    if (loading) return (<div>Loading...</div>);

    return (
        <div className="App">
            <form onSubmit={sendMessage}>
                <input
                    type="text"
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                    placeholder="Type a message..."
                />
                <button type="submit">Send</button>
            </form>
            <div className="chat-windows">
                <div className="chat-window">
                    {messages.map((message, index) => (
                        <textarea ref={chatWindow} key={index} value={message.text}
                                  className={`message ${message.sender}`}/>
                    ))}
                </div>
                <div className="chat-window">
                    {ragMessages.map((message, index) => (
                        <textarea ref={chatWindow} key={index} value={message.text}
                                  className={`message ${message.sender}`}/>
                    ))}
                </div>
            </div>
        </div>
    );
}

export default App;
