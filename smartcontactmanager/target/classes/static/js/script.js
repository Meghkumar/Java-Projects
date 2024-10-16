
const toggleSidebar = () => {
    const sidebar = $(".sidebar");
    const content = $(".content");

    if (sidebar.is(":visible")) {
        sidebar.hide();
        content.css("margin-left", "0%");
    } else {
        sidebar.show();
        content.css("margin-left", "20%");
    }
}

const search = () => {
    console.log("Searching...");
    let query = $("#search-input").val(); // Ensure the input has the correct id
    console.log("Query:", query); // Add this line to debug and see the query value

    if (query === '') {
        $(".search-result").hide();
    } else {
        console.log("Searching for:", query);

        let url = `http://localhost:8080/search/${query}`;
        fetch(url)
            .then(response => response.json())
            .then(data => {
                console.log(data);

                let text = `<div class='list-group'>`;

                data.forEach(contact => {
                    text += `<a href='/user/${contact.cid}/contact' class='list-group-item list-group-item-action'>${contact.name}</a>`;
                });

                text += `</div>`;
                $(".search-result").html(text).show();
            })
            .catch(error => console.error('Error:', error));
    }
}






