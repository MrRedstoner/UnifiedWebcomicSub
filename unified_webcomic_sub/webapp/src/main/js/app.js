'use strict';

const React = require('react');
const ReactDOM = require('react-dom');
import UserArea from './UserArea'

import { createRoot } from 'react-dom/client';

class App extends React.Component {
	render() {
		return (<UserArea />);
	}
}

createRoot(
	document.getElementById('react')
).render(
	<App />
)
