'use strict';

const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');
import { LoginLink, LogoutLink, RegisterLink } from './Links'

class App extends React.Component {

	constructor(props) {
		super(props);
		this.state = { employees: [] };
	}

	componentDidMount() {
		client({ method: 'GET', path: '/api/sources' }).done(response => {
			this.setState({ employees: response.entity._embedded.sources });
		});
	}

	render() {
		return (
			<EmployeeList employees={this.state.employees} />
		)
	}
}

class EmployeeList extends React.Component {
	render() {
		const employees = this.props.employees.map(employee =>
			<Employee key={employee._links.self.href} employee={employee} />
		);
		return (
			<>
				<LoginLink />
				<LogoutLink />
				<RegisterLink />
				<a href="/private.html">Logged in only</a>
				<table>
					<tbody>
						<tr>
							<th>Id</th>
							<th>Name</th>
							<th>Description</th>
						</tr>
						{employees}
					</tbody>
				</table>
			</>
		)
	}
}

class Employee extends React.Component {
	render() {
		return (
			<tr>
				<td>{this.props.employee.useId}</td>
				<td>{this.props.employee.name}</td>
				<td>{this.props.employee.description}</td>
			</tr>
		)
	}
}

ReactDOM.render(
	<App />,
	document.getElementById('react')
)
