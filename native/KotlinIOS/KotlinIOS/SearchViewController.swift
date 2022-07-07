import UIKit
import SharedCode

class StationSearchCell: UITableViewCell {
    @IBOutlet var stationName: UILabel!
    @IBOutlet var stationCrs: UILabel!
}

class SearchViewController: UIViewController, StationSearchContractView {
    @IBOutlet var searchTableView: UITableView!
    @IBOutlet var searchBar: UISearchBar!
    
    weak var delegate: SearchDelegate?
    private let stationSearchCell = "StationSearchCell"
    var stations: [StationDetails] = [StationDetails]()
    var filteredStations: [StationDetails] = []
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
        if let cell = searchTableView.dequeueReusableCell(withIdentifier: stationSearchCell, for: indexPath) as? StationSearchCell{ cell.stationName.text = filteredStations[indexPath.row].name
            cell.stationCrs.text = filteredStations[indexPath.row].crs
            return cell
        }
        
        return UITableViewCell()
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return filteredStations.count
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let stationCrs = filteredStations[indexPath.row].crs!
        self.delegate?.updateButtons(text: stationCrs, button: button!)
        dismiss(animated: true, completion: nil)
    }
    
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        
        filteredStations = searchText.isEmpty ? stations : stations.filter { (item: StationDetails) -> Bool in return checkStringContains(input:item.crs!, substring: searchText) || checkStringContains(input:item.name, substring: searchText)
        }
        
        searchTableView.reloadData()
    }
}

func checkStringContains(input: String, substring: String) -> Bool {
    return input.range(of: substring, options: .caseInsensitive, range: nil, locale: nil) != nil
}

protocol SearchDelegate : AnyObject {
    func updateButtons(text: String, button: UIButton)
}
