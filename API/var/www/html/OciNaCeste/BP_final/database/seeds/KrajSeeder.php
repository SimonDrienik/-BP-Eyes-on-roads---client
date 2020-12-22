<?php

use App\Models\Kraj;
use Illuminate\Database\Seeder;

class KrajSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        $kraje = [
            [
                'nazov' => 'Nepriradeny',
            ],
            [
                'nazov' => 'Trnavsky',
            ],
            [
                'nazov' => 'Bratislavsky',
            ],
            [
                'nazov' => 'Nitriansky',
            ]
        ];

        foreach ($kraje as $kraj) {
            Kraj::create(array(
                'nazov' => $kraj['nazov']
            ));
        }
    }
}
