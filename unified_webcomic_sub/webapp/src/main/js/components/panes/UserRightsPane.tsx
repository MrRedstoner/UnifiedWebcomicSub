'use strict';

import React from 'react';
import { Routes, Route } from 'react-router-dom';
import { UserPermissionClosure } from '../../api/entities';
import UserRightsList from '../list/UserRightsList';
import UserRightsDetail from '../detail/UserRightsDetail';

type Props = {
	user: UserPermissionClosure;
}

const UserRightsPane: React.FC<Props> = ({ user }) => {
	return (<><h2>Users</h2>
		<Routes>
			<Route path="show">
				<Route path=":id" element={<UserRightsDetail user={user} />} />
			</Route>
			<Route path="/" element={<UserRightsList />} />
		</Routes>
	</>);

}

export default UserRightsPane;