'use strict';

import React from 'react';
import { UWSUser } from '../../api/entities';

type Props = {
	user: UWSUser;
}

const HomePane: React.FC<Props> = ({ user }) => {
	return (<>
		<h1>Unified Webcomic Subscriber</h1>
		{user !== null &&
			<h2>Hello {user.name}</h2>}
		<p>About goes here</p>
	</>);
}

export default HomePane;