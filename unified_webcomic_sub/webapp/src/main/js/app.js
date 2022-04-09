'use strict';

const React = require('react');
const ReactDOM = require('react-dom');
import UserArea from './UserArea'

class App extends React.Component {

	render() {
		return (<UserArea />);
	}
}

ReactDOM.render(
	<App />,
	document.getElementById('react')
)
