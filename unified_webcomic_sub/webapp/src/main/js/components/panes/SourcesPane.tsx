'use strict';

import React from 'react'
import SourceList from '../list/SourceList'
import { UserPermissionClosure } from '../../api/entities';
import SourceCreate from '../create/SourceCreate';
import SourceDetail from '../detail/SourceDetail';
import { Routes, Route } from 'react-router-dom';

type Props = {
	user: UserPermissionClosure;
}

const SourcesPane: React.FC<Props> = ({ user }) => {
	return (<><h2>Sources</h2>
		<Routes>
			<Route path="show">
				<Route path=":id" element={<SourceDetail user={user} />} />
			</Route>
			<Route path="new" element={<SourceCreate user={user}/>} />
			<Route path="/" element={<SourceList user={user}/>} />
		</Routes>
	</>);
};

export default SourcesPane;