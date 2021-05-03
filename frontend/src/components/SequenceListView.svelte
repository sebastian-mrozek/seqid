<script lang="ts">
  import type { NumericSequence } from "../model";
  import SequenceView from "./SequenceView.svelte";

  export let sequences: NumericSequence[];
</script>

<div class="seq-list">
  <table class="striped">
    <thead>
      <tr>
        <th>Name @ Namespace</th>
        <th>Last Value</th>
        <th>Actions</th>
      </tr>
    </thead>
    <tbody>
      {#each sequences as sequence}
        <SequenceView {sequence} on:increment on:reset on:remove />
      {/each}
    </tbody>
  </table>
</div>

<style>
</style>
