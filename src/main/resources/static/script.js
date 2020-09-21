const container = document.querySelector("#container");
const URL = "http://www.omdbapi.com/";
const APIKEY = "e5fccd62";
const PARAMS = { search: "s", get: "i", apikey: "apikey", page: "page" };
let searchResults = [];
let searchMovieValue = "";
let pageNumber = 1;
let views = [];
let userName = null;
let password = null;
let allBookmarks = [];

const init = () => {
  views = [searchComponent()];
  getAllBookmarks();
  render(views);
};


const toggleBookmark = async (imdbID) => {
  allBookmarks.map((item) => item.omdbId).includes(imdbID)
    ? await deleteBookmark(imdbID)
    : await saveBookmark(imdbID);
};


const saveBookmark = async (imdbID) => {
  try {
    let response = await fetch(
      `http://localhost:8080/api/bookmark?movieId=${imdbID}`,
      {
        method: "POST",
        headers: { Authorization: localStorage.getItem("auth") },
      }
    );
    response = await response.text();
    console.log(response);
    await getAllBookmarks();
    await handleGetMovie(imdbID, PARAMS);
  } catch (error) {
    console.log(error);
  }
};


const deleteBookmark = async (imdbID) => {
  try {
    let response = await fetch(
      `http://localhost:8080/api/bookmark?movieId=${imdbID}`,
      {
        method: "DELETE",
        headers: { Authorization: localStorage.getItem("auth") },
      }
    );
    response = await response.text();
    console.log(response);
    await getAllBookmarks();
    await handleGetMovie(imdbID, PARAMS);
  } catch (error) {
    console.log(error);
  }
};


const getAllBookmarks = async () => {
  if (!localStorage.getItem("auth")) return;
  try {
    let response = await fetch(`http://localhost:8080/api/bookmark`, {
      method: "GET",
      headers: { Authorization: localStorage.getItem("auth") },
    });
    let bookmarks = await response.json();
    console.log(bookmarks);
    allBookmarks = bookmarks;
  } catch (error) {
    console.log(error);
  }
};


const handleUsername = (self) => {
  userName = self.value;
};

const handlePassword = (self) => {
  password = self.value;
};

const handleSearchInput = (self) => {
  searchMovieValue = self.value;
};


const handleLogin = async (userName, password) => {
  console.log("this runs");
  try {
    let response = await fetch("http://localhost:8080/auth/login", {
      method: "POST",
      body: JSON.stringify({ userName, password }),
      headers: { "Content-Type": "application/json" },
    });
    let token = await response.text();
    if (token === "User does not exist") {
      response = await fetch("http://localhost:8080/auth/register", {
        method: "POST",
        body: JSON.stringify({ userName, password }),
        headers: { "Content-Type": "application/json" },
      });
      token = await response.text();
      alert(token);
      await handleLogin(userName, password);
      return;
    }
    localStorage.setItem("auth", token);
    console.log(token);
    await getAllBookmarks();
    console.log(allBookmarks);
  } catch (error) {
    console.log(error);
  }
};


const handleSearchInputEnter = (self) => {
  if (this.event.key === "Enter") {
    console.log("movie: ", self.value);
    handleSearch(self.value, (pageNumber = 1), PARAMS);
  }
};


const handleSearchSubmission = () =>
  handleSearch(searchMovieValue, (pageNumber = 1), PARAMS);


const handleSearch = async (query, pageNumber, { search, apikey, page }) => {
  try {
    const response = await fetch(
      `${URL}?${search}=${query}&${apikey}=${APIKEY}&${page}=${pageNumber}`
    );
    const { Search, Response } = await response.json();
    if (Response === "True") {
      searchResults = [...Search];
    } else if (Response === "False") {
      pageNumber--;
    }

    
    views = [listViewComponent(searchResults)];
    render(views);
    console.log(search, pageNumber);
  } catch (error) {
    console.error(error);
  }
};


const handleGetMovie = async (id, { get, apikey }) => {
  try {
    const response = await fetch(`${URL}?${get}=${id}&${apikey}=${APIKEY}`);
    const movieData = await response.json();
    views = [detailsViewComponent(movieData)];
    render(views);
  } catch (error) {
    console.log(error);
  }
};

const render = (views) => (container.innerHTML = views.join(""));


const detailsViewComponent = ({
  Poster,
  Title,
  Rated,
  Runtime,
  Director,
  imdbID,
  Ratings: [imdb, rotten, metacritic],
}) => `<div id="details">
<div class="container-fluid shadow border my-5 p-3">
  <div class="row">
    <div class="col-auto">
      <img
        class="img-fluid"
        src=${Poster === "N/A" ? "https://via.placeholder.com/170x250" : Poster}
        alt=""
        srcset=""
      />
    </div>
    <div class="col">
      <div class="row">
        <div class="col-12"><h3>${Title}</h3></div>
        <div class="col-12 mt-2">
          Year
          <span class="mx-1 rounded bg-secondary px-1 text-light"
            >${Rated}</span
          ><span class="mx-1 rounded bg-secondary px-1 text-light"
            >${Runtime}</span
          >
        </div>
        <div class="col-12 mt-2 text-capitalize">
          ${Director}
        </div>
        <div class="col-12 mt-2 cursor" onclick="toggleBookmark('${imdbID}')">
          <span class="material-icons"> ${
            allBookmarks.map((item) => item.omdbId).includes(imdbID)
              ? "bookmark </span> Remove from"
              : "bookmark_border </span> Add to"
          } 
          Watch List
        </div>
      </div>
    </div>
    
    <div class="col-4"></div>
  </div>
</div>
</div>`;

const listViewCardComponent = ({
  Title,
  Year,
  Poster,
  imdbID,
}) => `  <div class="col-6">
<div class="container-fluid p-3 border shadow cursor" onclick="handleGetMovie('${imdbID}', PARAMS)">
  <div class="row">
    <div class="col-auto">
      <img 
      width="170"
      height="250"
        class="img-fluid"
        src=${Poster === "N/A" ? "https://via.placeholder.com/170x250" : Poster}
        alt=""
        srcset=""
      />
    </div>
    <div class="col">
      <h4>${Title}</h4>
      ${Year}
    </div>
  </div>
</div>
</div>`;


const listViewPaginationComponent = (page) => `<div class="row">
<div class="col-auto mx-auto">
  <nav aria-label="Page navigation example">
    <ul class="pagination">
    ${
      page > 1
        ? `<li class="page-item" onclick="handleSearch(searchMovieValue, --pageNumber, PARAMS);">
    <a class="page-link" href="#">Previous</a>
  </li>`
        : ""
    }
      <li class="page-item" onclick="handleSearch(searchMovieValue, ++pageNumber, PARAMS);">
        <a class="page-link" href="#">Next</a>
      </li>
    </ul>
  </nav>
</div>
</div>`;


const listViewComponent = (lists) => ` <div id="list">
<div class="row">
  <div class="col-auto mx-auto my-5 text-center text-capitalize">
    Results for "${searchMovieValue}"
  </div>
</div>
<div class="row">
${
  lists.map(listViewCardComponent).join("")
}
</div>
${listViewPaginationComponent(pageNumber)}
</div>`;

const searchComponent = () => `
<div id="search">
<div class="row">
  <div class="col-auto mx-auto mb-5 text-center text-capitalize">
    search for your favorite movies here
  </div>
</div>
<div class="row">
  <div class="col-8 mx-auto">
    <div class="input-group mb-3">
      <input
      onkeydown="handleSearchInputEnter(this)"
      onchange="handleSearchInput(this)"
        type="text"
        class="form-control"
        placeholder="movie search"
        aria-label="movie search"
        aria-describedby="button-addon2"
      />
      <div class="input-group-append">
        <button
        onclick="handleSearchSubmission()"
          class="btn btn-outline-secondary"
          type="button"
          id="button-addon2"
        >
          <span class="material-icons"> search </span>
        </button>
      </div>
    </div>
  </div>
</div>
</div>`;

init();
