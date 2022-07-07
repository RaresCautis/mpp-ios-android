import UIKit
import SharedCode

class StationSearchCell: UITableViewCell {
    @IBOutlet var stationName: UILabel!
}

class SearchViewController: UIViewController, StationSearchContractView {
    @IBOutlet var searchTableView: UITableView!
    @IBOutlet var searchBar: UISearchBar!
    
    weak var delegate: SearchDelegate?
    private let stationSearchCell = "StationSearchCell"
    var stations: [String] = [String]()
    var filteredStations: [String] = []
    private let presenter: StationSearchContractPresenter = StationSearchPresenter()
    weak var button: UIButton?
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.onViewTaken(view: self)
        filteredStations = stations
        searchBar.delegate = self
        setUpSearchTable()
    }
    
}

extension SearchViewController: UITableViewDataSource, UITableViewDelegate, UISearchBarDelegate {
    
    func setUpSearchTable() {
        self.searchTableView.delegate = self
        self.searchTableView.dataSource = self
        
        registerTableViewCells()
    }
    
    func registerTableViewCells() {
        let stationCell = UINib(nibName: stationSearchCell, bundle: nil)
        self.searchTableView.register(stationCell, forCellReuseIdentifier: stationSearchCell)
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) ->
        UITableViewCell {
            if let cell = searchTableView.dequeueReusableCell(withIdentifier: stationSearchCell, for: indexPath) as? StationSearchCell{ cell.stationName.text = filteredStations[indexPath.row]
            return cell
        }
        
        return UITableViewCell()
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return filteredStations.count
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let string = filteredStations[indexPath.row]
        self.delegate?.updateButtons(text: string, button: button!)
        dismiss(animated: true, completion: nil)
    }
    
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        
        filteredStations = searchText.isEmpty ? stations : stations.filter { (item: String) -> Bool in return item.range(of: searchText, options: .caseInsensitive, range: nil, locale: nil) != nil
        }
        
        searchTableView.reloadData()
    }
}

protocol SearchDelegate : AnyObject {
    func updateButtons(text: String, button: UIButton)
}
