
import React, { useState, useEffect } from 'react';
import fetchEffect from './api/apiCall'

/*type UWSUser={
	name:string;
};/**/

const UserNameDisplay/*: React.FC*/ = () => {
  const [error, setError] = useState(null);
  const [isLoaded, setIsLoaded] = useState(false);
  const [items, setItems] = useState/*<UWSUser>/**/(null);

  useEffect(fetchEffect("/rest/user/getlogged", setItems, setError, setIsLoaded), [])

  if (error) {
    return <div>Error: {error.message}</div>;
  } else if (!isLoaded) {
    return <div>Loading...</div>;
  } else {
    return (
      <div>{items?.name || "no user"}</div>
    );
  }
}

export default UserNameDisplay;