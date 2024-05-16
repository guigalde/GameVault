export function formatDate(dateString) {

    const date = new Date(`20${dateString.slice(-2)}-${dateString.slice(0, 2)}-01`);

    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();

    return `${day}-${month}-${year}`;
}