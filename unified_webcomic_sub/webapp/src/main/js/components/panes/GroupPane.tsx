'use strict';

import React from 'react'
import GroupList from '../list/GroupList'
import { UserPermissionClosure } from '../../api/entities';
import GroupDetail from '../detail/GroupDetail';
import GroupCreate from '../create/GroupCreate';
import { Routes, Route } from 'react-router-dom';

type Props = {
	user: UserPermissionClosure;
}

const GroupPane: React.FC<Props> = ({ user }) => {
	return (<><h2>Groups</h2>
		<Routes>
			<Route path="show">
				<Route path=":id" element={<GroupDetail user={user} />} />
			</Route>
			<Route path="new" element={<GroupCreate user={user}/>} />
			<Route path="/" element={<GroupList user={user}/>} />
		</Routes>
	</>);
};

export default GroupPane;